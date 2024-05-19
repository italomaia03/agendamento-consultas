package tech.ada.java.agendamentoconsultas.controllers;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;

import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import utils.IntegrationTestsExtension;
import utils.PatientTestExtension;
import utils.RedisProperties;
import utils.TestRedisConfiguration;

@SpringBootTest(classes = TestRedisConfiguration.class)
@ExtendWith({IntegrationTestsExtension.class , PatientTestExtension.class})
@Import(RedisProperties.class)
@TestMethodOrder(OrderAnnotation.class)
public class PatientControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private PatientRepository patientRepository;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
        String response = mvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .content("""
                                {
                                    "email": "test@test.com",
                                    "senha": "Senha_daNasa123"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        token = JsonParser
                .parseString(response)
                .getAsJsonObject()
                .getAsJsonObject()
                .get("token").getAsString();
    }

    @Test
    @Order(1)
    public void update_patientFindWithSuccess() throws Exception{

        PatientUpdateRequestDto update = new PatientUpdateRequestDto("nome", "test@test.com", "(99) 9999-9999");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
            MockMvcRequestBuilders.put("/api/v1/patients/8c0dcc19-70ac-411a-b0a6-cd09741e9e59")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertEquals("nome", patientRepository.findByUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59")).get().getNome());
        Assertions.assertEquals("test@test.com", patientRepository.findByUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59")).get().getEmail());
        Assertions.assertEquals("(99) 9999-9999", patientRepository.findByUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59")).get().getTelefone());
    }

    @Test
    @Order(2)
    public void update_patientWithNotValidRequestsNotSuccess() throws Exception{

        PatientUpdateRequestDto update = new PatientUpdateRequestDto("nome123", "test@test.com", "(99) 9999-999999");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
            MockMvcRequestBuilders.put("/api/v1/patients/8c0dcc19-70ac-411a-b0a6-cd09741e9e59")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(3)
    public void update_patientWithNotFielsRequestsNotSuccess() throws Exception{

        PatientUpdateRequestDto update = new PatientUpdateRequestDto("nome123", "", "(99) 9999-999999");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
            MockMvcRequestBuilders.put("/api/v1/patients/8c0dcc19-70ac-411a-b0a6-cd09741e9e59")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(4)
    public void delete_notDeletePatientFindMustBeSuccess() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders.delete("/api/v1/patients/8c0dcc19-70ac-411a-b0a6-cd09741e9e59")
                .header("Authorization", "Bearer " + token)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
