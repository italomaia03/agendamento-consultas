package tech.ada.java.agendamentoconsultas.service;

import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoResponse;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;

public interface PatientService {
    PatientDtoResponse createPatient(PatientDtoRequest request);
    void update(PatientUpdateRequestDto request, String email);
}
