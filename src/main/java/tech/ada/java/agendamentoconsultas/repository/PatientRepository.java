package tech.ada.java.agendamentoconsultas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.ada.java.agendamentoconsultas.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
}
