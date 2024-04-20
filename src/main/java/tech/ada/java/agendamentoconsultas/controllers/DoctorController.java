package tech.ada.java.agendamentoconsultas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.service.DoctorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor

public class DoctorController {

    private final DoctorService service;

    @GetMapping

    public List<Doctor> findAll(){

        return this.service.findALl();
    }

    @GetMapping("/{uuid}")
    public Doctor findByUuid(@PathVariable UUID uuid){
        return service.findByUuid(uuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor addDoctor(@RequestBody Doctor doctor){
        return service.addDoctor(doctor);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID uuid, @RequestBody Doctor doctor){
        service.update(uuid,doctor);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid){
        service.delete(uuid);
    }

}
