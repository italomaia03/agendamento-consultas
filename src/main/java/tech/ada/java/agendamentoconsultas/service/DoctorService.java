package tech.ada.java.agendamentoconsultas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.respository.DoctorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

public class DoctorService {
    private final DoctorRepository repository;
    public List<Doctor> findALl() {

        return this.repository.findAll();
    };

}
