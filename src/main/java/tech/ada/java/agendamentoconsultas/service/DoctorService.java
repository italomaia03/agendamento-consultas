package tech.ada.java.agendamentoconsultas.service;

import jakarta.transaction.Transactional;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    List<DoctorDtoResponse> findALl();

    DoctorDtoResponse findByUuid(UUID uuid);

    DoctorDtoResponse addDoctor(DoctorDtoRequest dto);

    void update(UUID uuid, DoctorDtoRequest newDoctor);

    @Transactional
    void delete(UUID uuid);
}
