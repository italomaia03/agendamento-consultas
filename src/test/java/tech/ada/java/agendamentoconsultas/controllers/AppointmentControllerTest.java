package tech.ada.java.agendamentoconsultas.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.ada.java.agendamentoconsultas.model.Appointment;
import tech.ada.java.agendamentoconsultas.model.enums.AppointmentStatus;
import tech.ada.java.agendamentoconsultas.repository.AppointmentRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import utils.ContainersConfig;
import utils.UserManagementExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(UserManagementExtension.class)
@Import(ContainersConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @Test
    public void testCreateAppointment() throws Exception {
        UUID patientUuid = UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae");
        UUID doctorUuid = UUID.fromString("b300b754-9042-433a-b0a2-f32364bc5498");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/patients/{patientUuid}/doctors/{doctorUuid}", patientUuid, doctorUuid)
                .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "appointmentDate": "2024-05-22",
                        "appointmentStartTime": "22:00",
                        "appointmentDescription": "Consulta de rotina"
                      }
                """))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.appointmentDescription").value("Consulta de rotina"));
    }

    @Test
    public void getAllAppointmentsByPatientUuidShouldSuccess() throws Exception {
        UUID patientUuid = UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae");
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/patients/{patientUuid}/appointments", patientUuid)
        .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllAppointmentsByNotExistPatientUuidShouldNotSuccess() throws Exception {
        UUID patientUuid = UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eab");
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/patients/{patientUuid}/appointments", patientUuid)
        .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteAppointmentWithStatusWaitingShouldNotSuccess () throws Exception {
        UUID uuid = UUID.randomUUID();
        UUID patientUuid = UUID.fromString("a1daf9c6-3b98-4a61-ba52-19a8e6517eae");
        UUID doctorUuid = UUID.fromString("b300b754-9042-433a-b0a2-f32364bc5498");

        Appointment appointment = new Appointment(
            1L,
            LocalDate.now().plusDays(1),
            uuid,
            LocalTime.of(10, 0), 
            LocalTime.of(11, 0),
            "Consulta de rotina",
            AppointmentStatus.WAITING,
            doctorRepository.findByUuid(doctorUuid).orElseThrow(()-> new RuntimeException()),
            patientRepository.findByUuid(patientUuid).orElseThrow(()-> new RuntimeException())            
            );
            appointmentRepository.save(appointment);
        
        
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/doctors/{doctorUuid}/appointments/{appointmentUuid}", doctorUuid, uuid)
            .header("Authorization", "Bearer " + UserManagementExtension.getAdminToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
                {
                    "appointmentStatus": "WAITING"
                }
        """))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
