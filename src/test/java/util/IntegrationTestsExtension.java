package util;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.ada.java.agendamentoconsultas.model.Admin;
import tech.ada.java.agendamentoconsultas.repository.AdminRepository;

public class IntegrationTestsExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var appContext = SpringExtension.getApplicationContext(extensionContext);
        var repository = appContext.getBean(AdminRepository.class);
        String encryptedPassword = new BCryptPasswordEncoder().encode("admin");
        Admin admin = new Admin();
        admin.setName("admin-test");
        admin.setPassword(encryptedPassword);
        admin.setEmail("admin@admin.com");
        repository.save(admin);
    }
}
