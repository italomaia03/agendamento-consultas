package tech.ada.java.agendamentoconsultas.service;

import java.util.List;
import java.util.UUID;

import tech.ada.java.agendamentoconsultas.model.Dto.*;

public interface PatientService {
    PatientDtoResponse createPatient(PatientDtoRequest request);
    void update(PatientUpdateRequestDto request, UUID uuid);
    void changePassword(PatientUpdatePasswordDto request, UUID uuid);

    List<PatientGetAllResponseDto> getAll();
}
