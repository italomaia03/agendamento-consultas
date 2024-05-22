package tech.ada.java.agendamentoconsultas.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.ada.java.agendamentoconsultas.annotation.ValidateUserPermission;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoResponse;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;
import tech.ada.java.agendamentoconsultas.service.PatientService;

import java.util.UUID;



@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Paciente")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).PATIENT.name())")
    @ValidateUserPermission
    public void update(@PathVariable UUID uuid, @RequestBody @Valid PatientUpdateRequestDto request){
        patientService.update(request, uuid);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).PATIENT.name())")
    @ValidateUserPermission
    public void deletePatient(@PathVariable UUID uuid){
        patientService.deletePatient(uuid);
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).ADMIN.name())")
    public PatientDtoResponse getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }
    
}
