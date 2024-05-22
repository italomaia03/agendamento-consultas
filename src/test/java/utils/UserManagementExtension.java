package utils;

import lombok.Getter;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Admin;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.AdminRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import tech.ada.java.agendamentoconsultas.security.service.TokenService;

import java.util.UUID;

public class UserManagementExtension implements BeforeAllCallback, AfterAllCallback {
    @Getter
    private static String adminToken;
    @Getter
    private static String doctorToken;
    @Getter
    private static String patientToken;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        var appContext = SpringExtension.getApplicationContext(extensionContext);
        var adminRepository = appContext.getBean(AdminRepository.class);
        var doctorRepository = appContext.getBean(DoctorRepository.class);
        var patientRepository = appContext.getBean(PatientRepository.class);
        var tokenService = appContext.getBean(TokenService.class);
        var encoder = new BCryptPasswordEncoder();
        var admin = registerAdmin(adminRepository, encoder);
        var doctor = registerDoctor(doctorRepository, encoder);
        var patient = registerPatient(patientRepository, encoder);

        adminToken = tokenService.generatedToken(admin);
        doctorToken = tokenService.generatedToken(doctor);
        patientToken = tokenService.generatedToken(patient);
    }

    private Admin registerAdmin(AdminRepository repository, BCryptPasswordEncoder passwordEncoder) {
        var user = new Admin();
        user.setName("admin-test");
        user.setUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59"));
        user.setPassword(passwordEncoder.encode("admin"));
        user.setEmail("admin@admin.com");
        return repository.save(user);
    }

    private Doctor registerDoctor(DoctorRepository repository, BCryptPasswordEncoder passwordEncoder) {
        var user = new Doctor();
        user.setName("doctor-test");
        user.setUuid(UUID.fromString("b300b754-9042-433a-b0a2-f32364bc5498"));
        user.setCrm("12345-CE");
        user.setPassword(passwordEncoder.encode("doctor"));
        user.setEmail("doctor@doctor.com");
        return repository.save(user);
    }

    private Patient registerPatient(PatientRepository repository, BCryptPasswordEncoder passwordEncoder) {
        Address address = new Address();
        address.setCep("03058-060");
        Patient user = new Patient(
                "User-test",
                "patient@patient.com",
                passwordEncoder.encode("Senha_daNasa123"),
                "123456",
                "document",
                address
        );
        user.setUuid(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae"));
        return repository.save(user);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        // var appContext = SpringExtension.getApplicationContext(extensionContext);
        // var adminRepository = appContext.getBean(AdminRepository.class);
        // var doctorRepository = appContext.getBean(DoctorRepository.class);
        // var patientRepository = appContext.getBean(PatientRepository.class);

        // adminRepository.deleteAll();
        // doctorRepository.deleteAll();
        // patientRepository.deleteAll();
    }
}
