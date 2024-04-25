package tech.ada.java.agendamentoconsultas.security.utils;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import tech.ada.java.agendamentoconsultas.security.service.TokenService;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final PatientRepository patientRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                var token = this.recoverToken(request);
                if(token != null){
                    var login = this.tokenService.validateToken(token);
                    UserDetails patient = patientRepository.findByEmail(login);

                    var authentication = new UsernamePasswordAuthenticationToken(patient, null, patient.getAuthorities());
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var autHeader = request.getHeader("Authorization");
        if(autHeader == null) return null;
        return autHeader.replace("Bearer ", "");
    }
    
}
