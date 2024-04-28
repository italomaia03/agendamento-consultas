package tech.ada.java.agendamentoconsultas.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginResponseDto;
import tech.ada.java.agendamentoconsultas.model.Token;
import tech.ada.java.agendamentoconsultas.repository.TokenRepository;
import tech.ada.java.agendamentoconsultas.security.service.TokenService;
import tech.ada.java.agendamentoconsultas.service.AuthService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextLogoutHandler logoutHandler;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;


    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generatedToken((UserDetails) auth.getPrincipal());
        return new LoginResponseDto(token);
    }

    @Override
    public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }
        final String token = header.substring(7);
        String username = tokenService.extractUsername(token);
        var expiration = tokenService.extractExpiration(token).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String key = username + "-" + expiration;
        Token tokenBlackListed = Token.builder()
                .key(key)
                .expires(Duration.between(LocalDateTime.now(), expiration).getSeconds())
                .build();
        tokenRepository.save(tokenBlackListed);
        logoutHandler.logout(request, response, authentication);
    }
}
