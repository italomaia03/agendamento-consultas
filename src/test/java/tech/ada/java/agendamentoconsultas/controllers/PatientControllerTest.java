package tech.ada.java.agendamentoconsultas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import utils.RedisContainerExtension;
import utils.UserManagementExtension;

import java.util.UUID;

@SpringBootTest
@ExtendWith({UserManagementExtension.class, RedisContainerExtension.class})
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PatientControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PatientRepository patientRepository;

    @Test
    @Order(1)
    public void update_patientFindWithSuccess() throws Exception{

        PatientUpdateRequestDto update = new PatientUpdateRequestDto("nome", "patient@patient.com", "(99) 9999-9999");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
            MockMvcRequestBuilders.put("/api/v1/patients/a1daf9c6-3b98-4a61-ba52-19a8e6517eae")
                .header("Authorization", "Bearer " + UserManagementExtension.getPatientToken())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertEquals("nome", patientRepository.findByUuid(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae")).get().getNome());
        Assertions.assertEquals("patient@patient.com", patientRepository.findByUuid(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae")).get().getEmail());
        Assertions.assertEquals("(99) 9999-9999", patientRepository.findByUuid(UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae")).get().getTelefone());
    }

    @Test
    @Order(2)
    public void update_patientWithNotValidRequestsNotSuccess() throws Exception{

        PatientUpdateRequestDto update = new PatientUpdateRequestDto("nome123", "patient@patient.com", "(99) 9999-999999");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
            MockMvcRequestBuilders.put("/api/v1/patients/a1daf9c6-3b98-4a61-ba52-19a8e6517eae")
                .header("Authorization", "Bearer " + UserManagementExtension.getPatientToken())
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
            MockMvcRequestBuilders.put("/api/v1/patients/a1daf9c6-3b98-4a61-ba52-19a8e6517eae")
                .header("Authorization", "Bearer " + UserManagementExtension.getPatientToken())
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
            MockMvcRequestBuilders.delete("/api/v1/patients/a1daf9c6-3b98-4a61-ba52-19a8e6517eae")
                .header("Authorization", "Bearer " + UserManagementExtension.getPatientToken())
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
