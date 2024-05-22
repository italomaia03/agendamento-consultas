package tech.ada.java.agendamentoconsultas.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import tech.ada.java.agendamentoconsultas.model.Appointment;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Patient;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AppointmentRepositoryIntegrationTest {
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    public void setup() {
        patient = new Patient();
        patient.setId(1L);
        patient.setUuid(UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab1"));
        patient.setEmail("teste@test.com");
        patient.setCpf("teste");
        patient.setTelefone("teste");
        patient.setSenha("teste");
        patient.setNome("teste");
        patient.setIsActive(true);
        patientRepository.save(patient);
        
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUuid(UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab2"));
        doctor.setName("teste");
        doctor.setCrm("teste");
        doctor.setPassword("teste");
        doctor.setEmail("teste@test.com");
        doctorRepository.save(doctor);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setUuid(UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab3"));
        appointment.setAppointmentDate(LocalDate.of(2024, 5, 30));
        appointment.setAppointmentStartTime(LocalTime.of(10, 0, 0));
        appointment.setAppointmentEndTime(LocalTime.of(11, 0, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointmentRepository.save(appointment);

    }
    
    @Test
    @DisplayName("return true if the appointment already exists")
    public void appointmentExistsShouldTrue() {

        Assertions.assertTrue( 
            appointmentRepository.appointmentExists(
                LocalDate.of(2024, 5, 30),
                UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab2"),
                LocalTime.of(10, 0, 0)
            )
        );
    }

    @Test
    @DisplayName("return false if the appointment not exists")
    public void appointmentNotExistsShouldFalse() {

        Assertions.assertFalse( 
            appointmentRepository.appointmentExists(
                LocalDate.of(2024, 5, 30),
                UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab3"),
                LocalTime.of(10, 0, 0)
            )
        );
    }

    @Test
    @DisplayName("return one appointment list by patient")
    public void searchAllAppointmentsByPatient() {
        Assertions.assertFalse(appointmentRepository.findAllByPatient(patient).isEmpty());
    }

    @Test
    @DisplayName("return one appointment list by doctorUuid")
    public void searchAllAppointmentsByDoctorUuid() {
        Assertions.assertFalse(appointmentRepository.findAllByDoctorUuid(UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab2")).isEmpty());
    }

    @Test
    @DisplayName("return one appointment list by patient")
    public void searchEmptyListToPatientWithAnyAppointments() {
        patient = new Patient();
        patient.setId(2L);
        patient.setUuid(UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab4"));
        patient.setEmail("teste1@test.com");
        patient.setCpf("teste");
        patient.setTelefone("teste");
        patient.setSenha("teste");
        patient.setNome("teste");
        patient.setIsActive(true);
        patientRepository.save(patient);
        Assertions.assertTrue(appointmentRepository.findAllByPatient(patient).isEmpty());
    }

    @Test
    @DisplayName("return appointment by doctor and appointment uuid")
    public void findAppointmentsByDoctorUuidAndAppointmentUuidSuccess() {
        Assertions.assertEquals(patient.getNome(),
            appointmentRepository.findByDoctorAndUuid(
                doctor, UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab3")
                ).get().getPatient().getNome()
        );
    }

    @Test
    @DisplayName("return appointment list by doctorUuid and appointment date")
    public void findAppointmentsByDoctorUuidAndAppointmentDateSuccess() {
        Assertions.assertFalse(
            appointmentRepository.findAllByDoctorUuidAndAppointmentDate(
                UUID.fromString("61fd2253-3c93-4d43-ba18-d1e4ecb13ab2"), 
                LocalDate.of(2024, 5, 30)
                ).isEmpty()
        );
    }

}   
