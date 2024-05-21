package tech.ada.java.agendamentoconsultas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import utils.RedisContainerExtension;
import utils.UserManagementExtension;

import java.util.stream.Stream;

@SpringBootTest
@ExtendWith({UserManagementExtension.class, RedisContainerExtension.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DoctorControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private DoctorRepository doctorRepository;

    static Stream<String> getFindAllValidTokens() {
        return Stream.of(UserManagementExtension.getAdminToken(), UserManagementExtension.getPatientToken());
    }

    static Stream<String> getFindByUuidValidTokens() {
        return Stream.of(UserManagementExtension.getAdminToken(), UserManagementExtension.getDoctorToken());
    }

    static Stream<String> getDeleteInvalidTokens() {
        return Stream.of(UserManagementExtension.getDoctorToken(), UserManagementExtension.getPatientToken());
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
    public void create_doctorWithAdminCredentials_shouldSucceed() throws Exception {
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

    @Order(1)
    @ParameterizedTest
    @MethodSource("getFindAllValidTokens")
    public void findAll_userWithCredentials_shouldSucceed(String token) throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/doctors")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("doctor-test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uuid").value("b300b754-9042-433a-b0a2-f32364bc5498"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].crm").value("12345-CE"));
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

    @Order(2)
    @Test
    public void findAll_userWithDoctorCredentials_shouldFail() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/doctors")
                                .header("Authorization", "Bearer " + UserManagementExtension.getDoctorToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void findByUuid_userWithoutCredentials_shouldFail() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/doctors/b300b754-9042-433a-b0a2-f32364bc5498")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource("getFindByUuidValidTokens")
    public void findByUuid_userWithValidCredentials_shouldSucceed(String token) throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/doctors/b300b754-9042-433a-b0a2-f32364bc5498")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Order(4)
    @Test
    public void findByUuid_validDoctorTryingToViewUuidDifferentFromItsOwn_shouldFail() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/doctors/8a2c4c22-1f49-4d98-8750-a94aa6940e49")
                                .header("Authorization", "Bearer " + UserManagementExtension.getDoctorToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Order(5)
    @ParameterizedTest
    @MethodSource("getFindByUuidValidTokens")
    public void update_userWithValidCredentials_shouldSucceed(String token) throws Exception {
        AddressRequestDto adress = new AddressRequestDto("58434-630", 0);
        DoctorDtoRequest update = new DoctorDtoRequest("nome", "doctor@doctor.com", "senhaDificil123@", "1234-CE", "proctologista", adress);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/doctors/b300b754-9042-433a-b0a2-f32364bc5498")
                                .header("Authorization", "Bearer " + token)
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

    @Order(6)
    @Test
    public void update_doctorWithValidCredentials_shouldNotUpdateDifferentDoctor() throws Exception {
        AddressRequestDto adress = new AddressRequestDto("58434-630", 0);
        DoctorDtoRequest update = new DoctorDtoRequest("nome", "doctor@doctor.com", "senhaDificil123@", "1234-CE", "proctologista", adress);
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(update);

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/doctors/8a2c4c22-1f49-4d98-8750-a94aa6940e49")
                                .header("Authorization", "Bearer " + UserManagementExtension.getDoctorToken())
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Order(7)
    @ParameterizedTest
    @MethodSource("getDeleteInvalidTokens")
    public void delete_userWithValidCredentialsNotAdmin_shouldFail(String token) throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/doctors/b300b754-9042-433a-b0a2-f32364bc5498")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Order(8)
    @Test
    public void delete_userWithValidAdminCredentials_shouldSucceed() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/doctors/b300b754-9042-433a-b0a2-f32364bc5498")
                                .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
