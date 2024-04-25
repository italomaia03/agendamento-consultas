package tech.ada.java.agendamentoconsultas.service;

import tech.ada.java.agendamentoconsultas.model.Dto.LoginRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto request);
    void logout();
}
