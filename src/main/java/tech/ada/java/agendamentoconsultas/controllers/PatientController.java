package tech.ada.java.agendamentoconsultas.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.http.HttpStatus;


import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoResponse;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;
import tech.ada.java.agendamentoconsultas.service.PatientService;


@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDtoResponse createPatient(@RequestBody @Valid PatientDtoRequest request){
        return patientService.createPatient(request);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid PatientUpdateRequestDto request, @PathVariable UUID uuid){
        patientService.update(request, uuid);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable UUID uuid){
        patientService.deletePatient(uuid);
    }
}
