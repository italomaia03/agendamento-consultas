package tech.ada.java.agendamentoconsultas.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.ada.java.agendamentoconsultas.model.Doctor;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUuid(UUID uuid);

    @Query("update Doctor set isActive = false where uuid = :uuid")
    void deleteByUuid(@Param("uuid")UUID uuid);


}


