package tech.ada.java.agendamentoconsultas.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tech.ada.java.agendamentoconsultas.model.Admin;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AdminRespositoryIntegrationTest {

    @Autowired
    private AdminRepository repository;

    @BeforeEach
    public void setUp() {
        var admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@admin.com");

        repository.save(admin);
    }

    @Test
    @DisplayName("find by e-mail with already registered e-mail should be successful")
    public void findByEmailSuccess() {
        var foundDoctor = repository.findByEmail("admin@admin.com");

        Assertions.assertTrue(foundDoctor.isPresent());
        Assertions.assertEquals("admin@admin.com", foundDoctor.get().getEmail());
    }

    @Test
    @DisplayName("find by e-mail with not registered e-mail should fail")
    public void findByEmailFail() {
        var foundDoctor = repository.findByEmail("test@admin.com");

        Assertions.assertTrue(foundDoctor.isEmpty());
    }
}
