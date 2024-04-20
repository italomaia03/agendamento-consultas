package tech.ada.java.agendamentoconsultas.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.ada.java.agendamentoconsultas.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {


}
