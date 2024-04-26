package tech.ada.java.agendamentoconsultas.service;

import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentDeleteRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentResponseDto create(AppointmentRequestDto request, UUID doctorUuid, UUID patientUuid);

    List<AppointmentResponseDto> findAllByPatientUuid(UUID patientUuid);

    List<AppointmentResponseDto> findAllByDoctorUuid(UUID doctorUuid);

    List<AppointmentResponseDto> findAllByDoctorUuidAndAppointmentDate(UUID doctorUuid, LocalDate date);

    void update(AppointmentRequestDto request, UUID doctorUuid, UUID appointmentUuid);

    void delete(AppointmentDeleteRequestDto request, UUID doctorUuid, UUID appointmentUuid);
}
