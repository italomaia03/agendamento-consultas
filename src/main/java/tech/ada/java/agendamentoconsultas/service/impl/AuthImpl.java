package tech.ada.java.agendamentoconsultas.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginResponseDto;
import tech.ada.java.agendamentoconsultas.security.service.TokenService;
import tech.ada.java.agendamentoconsultas.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    
    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        var usenamePassword = new UsernamePasswordAuthenticationToken(request.email(),request.senha());
        var auth = this.authenticationManager.authenticate(usenamePassword);

        var token = tokenService.generatedToken((Patient) auth.getPrincipal());
        return new LoginResponseDto(token);
    }

    @Override
    public void logout() {
        
    }
    
}
