package tech.ada.java.agendamentoconsultas.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import tech.ada.java.agendamentoconsultas.model.Appointment;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.AppointmentRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AppointmentImplUnitTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;
    private UUID doctorUuid;
    private UUID patientUuid;
    private AppointmentRequestDto request;
    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;
    private AppointmentResponseDto appointmentResponseDto;

    @BeforeEach
    void setUp() {
        doctorUuid = UUID.fromString("2269993d-1b46-4bf6-ae56-8182826661d9");
        patientUuid = UUID.fromString("5b97226b-e1a6-4c67-b3e4-558a4f6e2f70");

        doctor = new Doctor();
        patient = new Patient();
        request = new AppointmentRequestDto();
        request.setAppointmentDate(LocalDate.now().plusDays(1L));
        request.setAppointmentStartTime(LocalTime.of(10, 0));
        appointment = new Appointment();
        appointmentResponseDto = new AppointmentResponseDto();
        Mockito.when(doctorRepository.findByUuid(doctorUuid)).thenReturn(Optional.of(doctor));
        Mockito.when(patientRepository.findByUuid(patientUuid)).thenReturn(Optional.of(patient));
        Mockito.when(appointmentRepository.appointmentExists(Mockito.any(), Mockito.eq(doctorUuid), Mockito.any())).thenReturn(false);
        Mockito.when(modelMapper.map(request, Appointment.class)).thenReturn(appointment);

    }

    @Test
    public void create_appointment_createWithSuccessfullAppointment(){
        appointmentService.create(request, doctorUuid, patientUuid);
        Mockito.verify(appointmentRepository, Mockito.times(1)).save(appointment);
    }
}
