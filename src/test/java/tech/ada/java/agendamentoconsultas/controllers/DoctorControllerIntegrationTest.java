package tech.ada.java.agendamentoconsultas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import tech.ada.java.agendamentoconsultas.model.Dto.AddressRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import utils.DoctorTestsExtension;
import utils.IntegrationTestsExtension;
import utils.RedisProperties;
import utils.TestRedisConfiguration;

@SpringBootTest(classes = TestRedisConfiguration.class)
@ExtendWith({IntegrationTestsExtension.class, DoctorTestsExtension.class})
@Import(RedisProperties.class)
public class DoctorControllerIntegrationTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
        String response = mvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .content("""
                                {
                                    "email": "admin@admin.com",
                                    "senha": "admin"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        token = JsonParser
                .parseString(response)
                .getAsJsonObject()
                .getAsJsonObject()
                .get("token").getAsString();
    }

    @Test
    public void create_doctorWithoutCredentials_shouldThrowException() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/doctors")
                                .content("")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void create_doctorWithCredentials_shouldSucceed() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/doctors")
                                .header("Authorization", "Bearer " + token)
                                .content("""
                                        {
                                            "name": "Test",
                                            "email": "test@test.com",
                                            "password": "senha_Dificil123",
                                            "crm": "1234-CE",
                                            "specialty": "cardiologista",
                                            "address": {
                                                "cep": "65945970",
                                                "numero": 123
                                            }
                                        }
                                        """)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void findAll_userWithCredentials_shouldSucceed() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/doctors")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findAll_userWithoutCredentials_shouldFail() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/doctors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void update_doctorFindWithSuccess() throws Exception {
        AddressRequestDto adress = new AddressRequestDto("58434-630", 0);
        DoctorDtoRequest update = new DoctorDtoRequest("nome", "test@test.com", "senhaDificil123@", "1234-CE", "proctologista", adress);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/doctors/8c0dcc19-70ac-411a-b0a6-cd09741e9e51")
                                .header("Authorization", "Bearer " + token)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
//        Assertions.assertEquals("nome", DoctorRepository.findByUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59")).get().getNome());
//        Assertions.assertEquals("test@test.com", DoctorRepository.findByUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59")).get().getEmail());
//        Assertions.assertEquals("(99) 9999-9999", DoctorRepository.findByUuid(UUID.fromString("8c0dcc19-70ac-411a-b0a6-cd09741e9e59")).get().getTelefone());
    }
}
