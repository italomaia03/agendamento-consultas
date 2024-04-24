package tech.ada.java.agendamentoconsultas.service;

import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;

import java.util.UUID;

public interface AppointmentService {
    AppointmentResponseDto create(AppointmentRequestDto request, UUID doctorUuid, UUID patientUuid);
}
