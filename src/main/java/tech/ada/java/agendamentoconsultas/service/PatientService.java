package tech.ada.java.agendamentoconsultas.service;

import java.util.UUID;

import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoResponse;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdatePasswordDto;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;

public interface PatientService {
    PatientDtoResponse createPatient(PatientDtoRequest request);
    void update(PatientUpdateRequestDto request, UUID uuid);
    void changePassword(PatientUpdatePasswordDto request, UUID uuid);
}
