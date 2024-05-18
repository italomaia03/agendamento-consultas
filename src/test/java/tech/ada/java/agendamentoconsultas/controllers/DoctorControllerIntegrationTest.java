package tech.ada.java.agendamentoconsultas.controllers;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import util.IntegrationTestsExtension;

@SpringBootTest
@ExtendWith(IntegrationTestsExtension.class)
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
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
                        .content("""
                                {
                                    "name": "Test"
                                    "email": "test@test.com",
                                    "password": "test",
                                    "crm": "1234-CE",
                                    "specialty": "cardiologista",
                                    "address": {
                                        "cep": "1234567-32",
                                        "numero": 123
                                    }
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
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
                                    "password": "test",
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
}
