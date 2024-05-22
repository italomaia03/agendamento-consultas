package tech.ada.java.agendamentoconsultas.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Patient;

import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PatientRespositoryIntegrationTest {

    @Autowired
    private PatientRepository repository;

    @BeforeEach
    public void setUp() {
        Address address = new Address();
        address.setCep("1234");
        Patient user = new Patient(
                "User-test",
                "patient@patient.com",
                "senha",
                "123456",
                "document",
                address
        );
        user.setId(1L);
        user.setUuid(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae"));
        repository.save(user);
    }

    @Test
    public void find_patientAlreadyRegisteredByUuid_shouldReturnPatient() {
        var foundPatient = repository.findByUuid(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae"));

        Assertions.assertTrue(foundPatient.isPresent());
        Assertions.assertEquals(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae"), foundPatient.get().getUuid());
    }

    @Test
    public void find_patientNotRegisteredByUuid_shouldEmptyOptional() {
        var foundPatient = repository.findByUuid(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517ea1"));

        Assertions.assertTrue(foundPatient.isEmpty());
    }

    @Test
    public void find_patientAlreadyRegisteredByEmail_shouldReturnPatient() {
        var foundPatient = repository.findByEmail("patient@patient.com");

        Assertions.assertTrue(foundPatient.isPresent());
        Assertions.assertEquals("patient@patient.com", foundPatient.get().getEmail());
    }

    @Test
    public void find_patientNotRegisteredByEmail_shouldReturnEmptyOptional() {
        var foundPatient = repository.findByEmail("patient@test.com");

        Assertions.assertTrue(foundPatient.isEmpty());
    }
}
