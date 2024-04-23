package tech.ada.java.agendamentoconsultas.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.ada.java.agendamentoconsultas.model.Dto.*;
import tech.ada.java.agendamentoconsultas.service.PatientService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/sing-in")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDtoResponse createPatient(@RequestBody @Valid PatientDtoRequest request) {
        return patientService.createPatient(request);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid PatientUpdateRequestDto request, @PathVariable UUID uuid) {
        patientService.update(request, uuid);
    }

    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid PatientUpdatePasswordDto request, @PathVariable UUID uuid) {
        patientService.changePassword(request, uuid);
    }

    @GetMapping
    public List<PatientGetAllResponseDto> getAll() {
        return patientService.getAll();
    }
}
