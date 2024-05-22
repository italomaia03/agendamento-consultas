package tech.ada.java.agendamentoconsultas.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import tech.ada.java.agendamentoconsultas.model.Token;
import utils.ContainersConfig;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Import(ContainersConfig.class)
public class TokenRepositoryIntegrationTest {

    @Autowired
    private TokenRepository repository;

    @BeforeEach
    public void setUp() {
        Token token = Token.builder()
                .id(1L)
                .token("test")
                .key("test")
                .build();
        repository.save(token);
    }

    @Test
    public void find_tokenAlreadyRegisteredByKey_shoudReturnToken() {
        var foundToken = repository.findByKey("test");

        Assertions.assertTrue(foundToken.isPresent());
        Assertions.assertEquals("test", foundToken.get().getToken());
    }

    @Test
    public void find_tokenNotRegisteredByKey_shoudReturnEmptyOptional() {
        var foundToken = repository.findByKey("invalid-key");

        Assertions.assertTrue(foundToken.isEmpty());
    }
}
