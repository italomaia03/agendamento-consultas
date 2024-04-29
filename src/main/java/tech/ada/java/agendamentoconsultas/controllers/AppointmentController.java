package tech.ada.java.agendamentoconsultas.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.ada.java.agendamentoconsultas.annotation.ValidateUserPermission;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentDeleteRequestDto;
import tech.ada.java.agendamentoconsultas.service.AppointmentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Consulta")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping ("/patients/{patientUuid}/appointments")
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).PATIENT.name())")
    @ValidateUserPermission
    public List<AppointmentResponseDto> findAllByPatientUuid(@PathVariable UUID patientUuid) {
        return appointmentService.findAllByPatient(patientUuid);
    }

    @GetMapping ("/doctors/{doctorUuid}/appointments")
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).DOCTOR.name())")
    @ValidateUserPermission
    public List<AppointmentResponseDto> findAllByDoctorUuid(@PathVariable UUID doctorUuid, @RequestParam("exact-date") Optional<LocalDate> date) {
        if(date.isPresent()) {
         return appointmentService.findAllByDoctorUuidAndAppointmentDate(doctorUuid, date.get());
        }
        return appointmentService.findAllByDoctorUuid(doctorUuid);
    }

    @PostMapping("/patients/{patientUuid}/doctors/{doctorUuid}")
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).PATIENT.name())")
    @ValidateUserPermission
    public AppointmentResponseDto create(@PathVariable UUID patientUuid, @RequestBody @Valid AppointmentRequestDto request, @PathVariable UUID doctorUuid) {
        return appointmentService.create(request, doctorUuid, patientUuid);
    }

    @PutMapping("/doctors/{doctorUuid}/appointments/{appointmentUuid}")
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).DOCTOR.name())")
    @ValidateUserPermission
    public void updateDoctorAppointment(@PathVariable UUID doctorUuid, @RequestBody @Valid AppointmentRequestDto request, @PathVariable UUID appointmentUuid) {
        appointmentService.update(request, doctorUuid, appointmentUuid);
    }

    @DeleteMapping("/doctors/{doctorUuid}/appointments/{appointmentUuid}")
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).DOCTOR.name())")
    @ValidateUserPermission
    public void update(@PathVariable UUID doctorUuid, @RequestBody @Valid AppointmentDeleteRequestDto request, @PathVariable UUID appointmentUuid) {
        appointmentService.delete(request, doctorUuid, appointmentUuid);
    }
}
