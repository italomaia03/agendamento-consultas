package tech.ada.java.agendamentoconsultas.controllers;


import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.ada.java.agendamentoconsultas.service.AuthService;
import tech.ada.java.agendamentoconsultas.service.PatientService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
    @MockBean
    private PatientService patientService;

    private String baseUrl = "/api/v1/auth";

    @Test
    @Order(1)
    public void sign_in_patient_createPatientWithSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/sign-up")
            .content("""
                {
                    "nome": "Maria",
                    "email": "test@test.com",
                    "senha": "senha_Dificil123",
                    "telefone": "(99) 9999-9999",
                    "cpf": "83975404078",
                    "addressRequestDto": {
                        "cep": "60340-285",
                        "numero": 0
                    }
                }
            """)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void sign_in_patient_createPatientWithNameNotValidMustNotSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/sign-up")
            .content("""
                {
                    "nome": "Maria1",
                    "email": "test1@test.com",
                    "senha": "senha_Dificil123",
                    "telefone": "(99) 9999-9999",
                    "cpf": "55839721069",
                    "addressRequestDto": {
                        "cep": "60340-285",
                        "numero": 1
                    }
                }
            """)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void sign_in_patient_createPatientWithEmailNotValidMustNotSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/sign-up")
            .content("""
                {
                    "nome": "Maria",
                    "email": "test1",
                    "senha": "senha_Dificil123",
                    "telefone": "(99) 9999-9999",
                    "cpf": "55839721069",
                    "addressRequestDto": {
                        "cep": "60340-285",
                        "numero": 1
                    }
                }
            """)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Senh@1", 
    "Senha_MuitoLonga123456789_Com_Mais_de_32Caracteres", 
    "Senha_com_caracteresInvalidos$", 
    "senha@234", 
    "SENHA@123",
    "SENHADANASA123"})
    public void sign_in_patient_createPatientWithPasswordNotValidMustNotSuccess(String invalidPassword) throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/sign-up")
            .content("""
                {
                    "nome": "Maria",
                    "email": "test3@test.com",
                    "senha": "%s",
                    "telefone": "(99) 9999-9999",
                    "cpf": "55839721069",
                    "addressRequestDto": {
                        "cep": "60340-285",
                        "numero": 1
                    }
                }
            """.formatted(invalidPassword))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)

            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"xx", "XXxxxxxxxx","XXxxxxxxxxx", "XXxxxxxxxxxx","(XX) XXXXX-XXXXX"})
    public void sign_in_patient_createPatientWithPhoneNotValidMustNotSuccess(String invalidPhone) throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/sign-up")
            .content("""
                {
                    "nome": "Maria",
                    "email": "test4@test.com",
                    "senha": "senha_Dificil123",
                    "telefone": "%s",
                    "cpf": "55839721069",
                    "addressRequestDto": {
                        "cep": "60340-285",
                        "numero": 1
                    }
                }
            """.formatted(invalidPhone))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(2)
    public void login_AuthValidPatientMustSuccess() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.post(baseUrl + "/login")
            .content("""
                {
                    "email": "test@test.com",
                    "senha": "senha_Dificil123"
                }
            """)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
