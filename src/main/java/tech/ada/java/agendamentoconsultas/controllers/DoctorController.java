package tech.ada.java.agendamentoconsultas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public List<DoctorDtoResponse> findAll(){
        return this.service.findALl();
    }

    @GetMapping("/{uuid}")
    public DoctorDtoResponse findByUuid(@PathVariable UUID uuid){
        return service.findByUuid(uuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDtoResponse addDoctor(@RequestBody DoctorDtoRequest dto){
        return service.addDoctor(dto);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID uuid, @RequestBody DoctorDtoRequest dto){
        service.update(uuid,dto);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uuid){
        service.delete(uuid);
    }

}
