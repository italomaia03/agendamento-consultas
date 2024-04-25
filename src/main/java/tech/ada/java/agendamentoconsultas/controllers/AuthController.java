package tech.ada.java.agendamentoconsultas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginResponseDto;
import tech.ada.java.agendamentoconsultas.security.service.TokenService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto request) {
        var usenamePassword = new UsernamePasswordAuthenticationToken(request.email(),request.senha());
        var auth = this.authenticationManager.authenticate(usenamePassword);

        var token = tokenService.generatedToken((Patient) auth.getPrincipal());
        return new LoginResponseDto(token);
    }
    
}
