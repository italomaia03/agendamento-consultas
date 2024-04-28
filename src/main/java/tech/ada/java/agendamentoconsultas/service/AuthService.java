package tech.ada.java.agendamentoconsultas.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto request);
    void logout(Authentication authentication, HttpServletRequest request , HttpServletResponse response);
}
