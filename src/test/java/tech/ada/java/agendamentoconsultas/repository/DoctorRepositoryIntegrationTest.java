package tech.ada.java.agendamentoconsultas.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tech.ada.java.agendamentoconsultas.model.Doctor;

import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DoctorRepositoryIntegrationTest {
    @Autowired
    private DoctorRepository repository;

    private Doctor doctor;

    @BeforeEach
    public void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("test");
        doctor.setEmail("test@test.com");
        doctor.setPassword("test");
        doctor.setCrm("test");
        doctor.setUuid(UUID.fromString("607fa2df-fa84-4420-bf97-1a853ad23337"));
        repository.save(doctor);
    }

    @Test
    public void find_doctorAlreadyRegisteredByUuid_shouldReturnDoctor() {
        var foundDoctor = repository.findByUuid(UUID.fromString("607fa2df-fa84-4420-bf97-1a853ad23337"));

        Assertions.assertTrue(foundDoctor.isPresent());
        Assertions.assertEquals(UUID.fromString("607fa2df-fa84-4420-bf97-1a853ad23337"), foundDoctor.get().getUuid());
    }

    @Test
    public void find_doctorNotRegisteredByUuid_shouldReturnEmptyOptional() {
        var foundDoctor = repository.findByUuid(UUID.fromString("607fa2df-fa84-4420-bf97-1a853ad23333"));

        Assertions.assertTrue(foundDoctor.isEmpty());
    }

    @Test
    public void find_doctorAlreadyRegisteredByEmail_shouldReturnReturnDoctor() {
        var foundDoctor = repository.findByEmail("test@test.com");

        Assertions.assertTrue(foundDoctor.isPresent());
        Assertions.assertEquals("test@test.com", foundDoctor.get().getEmail());
    }

    @Test
    public void find_doctorNotRegisteredByEmail_shouldReturnEmptyOptional() {
        var foundDoctor = repository.findByEmail("invalid-email");

        Assertions.assertTrue(foundDoctor.isEmpty());
    }

    @Test
    public void verify_registeredDoctorWhendeleted_shouldNotBeActive() {
        repository.delete(doctor);
        Assertions.assertFalse(repository.existsById(doctor.getId()));
    }

    @Test
    public void verify_doctorAlreadyRegistered_shouldReturnTrue() {
        var foundDoctor = repository.existsByUuid(UUID.fromString("607fa2df-fa84-4420-bf97-1a853ad23337"));

        Assertions.assertTrue(foundDoctor);
    }

    @Test
    public void verify_doctorNotRegistered_shouldReturnFalse() {
        var foundDoctor = repository.existsByUuid(UUID.fromString("607fa2df-fa84-4420-bf97-1a853ad23333"));

        Assertions.assertFalse(foundDoctor);
    }
}
