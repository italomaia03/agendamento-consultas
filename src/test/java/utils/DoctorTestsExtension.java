package utils;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;

import java.util.UUID;

public class DoctorTestsExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var appContext = SpringExtension.getApplicationContext(extensionContext);
        var repository = appContext.getBean(DoctorRepository.class);
        String encryptedPassword = new BCryptPasswordEncoder().encode("Senha_daNasa123");
        Doctor doctor = new Doctor();
        doctor.setUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e51"));
        doctor.setId(1L);
        doctor.setPassword(encryptedPassword);
        doctor.setEmail("test@test.com.br");
        repository.save(doctor);
    }
}
