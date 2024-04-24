package tech.ada.java.agendamentoconsultas.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;
import tech.ada.java.agendamentoconsultas.service.AppointmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping ("/patients/{patientUuid}/appointments")
    public List<AppointmentResponseDto> findAllByPatientUuid(@PathVariable UUID patientUuid) {
        return appointmentService.findAllByPatientUuid(patientUuid);
    }

    @GetMapping ("/doctors/{doctorUuid}/appointments")
    public List<AppointmentResponseDto> findAllByDoctorUuid(@PathVariable UUID doctorUuid) {
        return appointmentService.findAllByDoctorUuid(doctorUuid);
    }

    @PostMapping("/patients/{patientUuid}/doctors/{doctorUuid}")
    public AppointmentResponseDto create(@RequestBody @Valid AppointmentRequestDto request, @PathVariable UUID doctorUuid, @PathVariable UUID patientUuid) {
        return appointmentService.create(request, doctorUuid, patientUuid);
    }

}
