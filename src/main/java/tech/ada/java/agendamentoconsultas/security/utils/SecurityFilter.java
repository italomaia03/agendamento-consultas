package tech.ada.java.agendamentoconsultas.security.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.ada.java.agendamentoconsultas.repository.TokenRepository;
import tech.ada.java.agendamentoconsultas.security.service.TokenService;

import java.io.IOException;
import java.time.ZoneId;

@Log4j2
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authHeader.substring(7);
        if(isTokenInBlackList(token)) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           return;
        }
        authenticateUserFromToken(request, token);
        filterChain.doFilter(request, response);
    }

    private void authenticateUserFromToken(HttpServletRequest request, String token) {
        final String username = this.tokenService.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenService.isTokenValid(token, userDetails)) {
                setAuthTokenOnSecurityContext(request, userDetails);
            }
        }
    }

    private Boolean isTokenInBlackList(String token) {
        String username = tokenService.extractUsername(token);
        var expiration = tokenService.extractExpiration(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String key = username + "-" + expiration;
        return tokenRepository.findByKey(key).isPresent();
    }

    private void setAuthTokenOnSecurityContext(HttpServletRequest request, UserDetails userDetails) {
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
