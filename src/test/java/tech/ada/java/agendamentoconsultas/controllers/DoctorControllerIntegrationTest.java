package tech.ada.java.agendamentoconsultas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import utils.UserManagementExtension;

@SpringBootTest
@ExtendWith(UserManagementExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DoctorControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private DoctorRepository doctorRepository;
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
                                .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
                                .content("""
                                        {
                                            "name": "Test",
                                            "email": "doctor@test.com",
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
                                .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
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
        DoctorDtoRequest update = new DoctorDtoRequest("nome", "doctor@test.com", "senhaDificil123@", "1234-CE", "proctologista", adress);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/doctors/b300b754-9042-433a-b0a2-f32364bc5498")
                                .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        var result = doctorRepository.findById(1L).orElseThrow();
        Assertions.assertEquals("nome", result.getName());
        Assertions.assertEquals("1234-CE", result.getCrm());
        Assertions.assertEquals("proctologista", result.getSpecialty());
    }
}
