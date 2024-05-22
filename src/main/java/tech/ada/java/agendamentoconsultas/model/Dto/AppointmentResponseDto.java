package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDto implements Serializable {
    private LocalDate appointmentDate;
    private LocalTime appointmentStartTime;
    private LocalTime appointmentEndTime;
    private String appointmentStatus;
    private String appointmentDescription;
    private DoctorAppointmentResponseDto doctor;
    private PatientAppointmentResponseDto patient;
    private UUID uuid;
}