package tech.ada.java.agendamentoconsultas.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoResponse;
import tech.ada.java.agendamentoconsultas.service.DoctorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor

public class DoctorController {

    private final DoctorService service;

    @GetMapping
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).PATIENT.name())")
    public List<DoctorDtoResponse> findAll(){
        return this.service.findALl();
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).DOCTOR.name())")
    public DoctorDtoResponse findByUuid(@PathVariable UUID uuid){
        return service.findByUuid(uuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).ADMIN.name())")
    public DoctorDtoResponse addDoctor(@Valid @RequestBody DoctorDtoRequest dto){
        return service.addDoctor(dto);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).DOCTOR.name())")
    public void update(@PathVariable UUID uuid, @Valid @RequestBody DoctorDtoRequest dto){
        service.update(uuid,dto);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole(T(tech.ada.java.agendamentoconsultas.model.enums.UserRole).ADMIN.name())")
    public void delete(@PathVariable UUID uuid){
        service.delete(uuid);
    }

}
