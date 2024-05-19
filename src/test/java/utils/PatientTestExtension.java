package utils;

import java.util.UUID;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;

public class PatientTestExtension implements BeforeAllCallback{

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var appContext = SpringExtension.getApplicationContext(extensionContext);
        var repository = appContext.getBean(PatientRepository.class);
        String encryptedPassword = new BCryptPasswordEncoder().encode("Senha_daNasa123");
        Address address = new Address();
        address.setCep("03058-060");
        Patient patient = new Patient(
            "User-test",
            "test@test.com",
            encryptedPassword,
            "123456",
            "document",
            address    
        );
        patient.setUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59"));
        patient.setIsActive(true);
        repository.save(patient);
    }
    
}
