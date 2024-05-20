package utils;

import lombok.Getter;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.ada.java.agendamentoconsultas.model.Admin;
import tech.ada.java.agendamentoconsultas.repository.AdminRepository;
import tech.ada.java.agendamentoconsultas.security.service.TokenService;

import java.util.UUID;

public class AdminIntegrationTestsExtension implements BeforeAllCallback {
    @Getter
    private static String token;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var appContext = SpringExtension.getApplicationContext(extensionContext);
        var repository = appContext.getBean(AdminRepository.class);
        var tokenService = appContext.getBean(TokenService.class);
        String encryptedPassword = new BCryptPasswordEncoder().encode("admin");
        Admin admin = new Admin();
        admin.setName("admin-test");
        admin.setUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59"));
        admin.setPassword(encryptedPassword);
        admin.setEmail("admin@admin.com");
        repository.save(admin);
        token = tokenService.generatedToken(admin);
    }

}
