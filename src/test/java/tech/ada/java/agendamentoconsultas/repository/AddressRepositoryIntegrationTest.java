package tech.ada.java.agendamentoconsultas.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tech.ada.java.agendamentoconsultas.model.Address;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddressRepositoryIntegrationTest {

    @Autowired
    private AddressRepository repository;

    @BeforeEach
    public void setUp() {
        var address = new Address(1L, "test-cep", "test", "test", 123, "test", "test", "TEST", "test", "test", "99", "test");
        repository.save(address);
    }

    @Test
    @DisplayName("find address by cep and number with valid parameters should be successful")
    public void findByCepAndNumberSuccess() {
        var foundAddress = repository.findByCepAndNumero("test-cep", 123).get();

        Assertions.assertEquals("test-cep", foundAddress.getCep());
        Assertions.assertEquals(123, foundAddress.getNumero());
    }

    @Test
    @DisplayName("find address by cep and number with non-existent CEP should fail")
    public void findByCepAndNumberInvalidCep() {
        var foundAddress = repository.findByCepAndNumero("invalid-cep", 123);

        Assertions.assertFalse(foundAddress.isPresent());
    }

    @Test
    @DisplayName("find address by cep and number with non-existent numero should fail")
    public void findByCepAndNumberInvalidNumber() {
        var foundAddress = repository.findByCepAndNumero("test-cep", 321);

        Assertions.assertFalse(foundAddress.isPresent());
    }

    @Test
    @DisplayName("find address by cep with valid parameters should be successful")
    public void findByCepSuccess() {
        var foundAddress = repository.findByCep("test-cep").get();

        Assertions.assertEquals("test-cep", foundAddress.getCep());
    }

    @Test
    @DisplayName("find address by cep with non-existent CEP should fail")
    public void findByCepFail() {
        var foundAddress = repository.findByCep("invalid-cep");

        Assertions.assertFalse(foundAddress.isPresent());
    }


}
