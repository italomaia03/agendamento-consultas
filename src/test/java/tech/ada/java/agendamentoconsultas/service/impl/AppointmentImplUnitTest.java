package tech.ada.java.agendamentoconsultas.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import tech.ada.java.agendamentoconsultas.exception.DoctorNotFoundException;
import tech.ada.java.agendamentoconsultas.exception.PatientNotFoundException;
import tech.ada.java.agendamentoconsultas.model.Appointment;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentDeleteRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;
import tech.ada.java.agendamentoconsultas.model.enums.AppointmentStatus;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.AppointmentRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private UUID appointmentUuid;
    private AppointmentRequestDto request;
    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;
    private AppointmentDeleteRequestDto deleteDto;
    private AppointmentResponseDto response;
    
    @BeforeEach
    void setUp() {

        doctorUuid = UUID.fromString("2269993d-1b46-4bf6-ae56-8182826661d9");
        patientUuid = UUID.fromString("5b97226b-e1a6-4c67-b3e4-558a4f6e2f70");
        appointmentUuid = UUID.fromString("5b97226b-e1a6-4c67-b3e4-558a4f6e2f71");
    
        doctor = new Doctor();
        patient = new Patient();
    
        request = new AppointmentRequestDto();
        request.setAppointmentDate(LocalDate.now().plusDays(1L));
        request.setAppointmentStartTime(LocalTime.of(10, 0));
    
        deleteDto = new AppointmentDeleteRequestDto();
        deleteDto.setAppointmentStatus(AppointmentStatus.CANCELLED);
    
        appointment = new Appointment();
        response = new AppointmentResponseDto();
    
        Mockito.when(appointmentRepository.findByDoctorAndUuid(doctor, appointmentUuid)).thenReturn(Optional.of(appointment));
        Mockito.when(appointmentRepository.findByDoctorAndUuid(doctor, UUID.fromString("11111111-1b46-4bf6-ae56-000000000000"))).thenReturn(Optional.empty());
    
        Mockito.when(doctorRepository.findByUuid(doctorUuid)).thenReturn(Optional.of(doctor));
        Mockito.when(doctorRepository.findByUuid(UUID.fromString("11111111-1b46-4bf6-ae56-000000000000"))).thenReturn(Optional.empty());
        
        Mockito.when(patientRepository.findByUuid(patientUuid)).thenReturn(Optional.of(patient));
        Mockito.when(patientRepository.findByUuid(UUID.fromString("11111111-1b46-4bf6-ae56-000000000000"))).thenReturn(Optional.empty());
        
        Mockito.when(appointmentRepository.appointmentExists(
            Mockito.any(), Mockito.eq(doctorUuid), Mockito.any())).thenReturn(false);
        
        Mockito.when(appointmentRepository.appointmentExists(
            Mockito.any(), Mockito.eq(UUID.fromString("11111111-1b46-4bf6-ae56-000000000000")), Mockito.any())).thenReturn(true);
        
        Mockito.when(modelMapper.map(request, Appointment.class)).thenReturn(appointment);
    
        Mockito.when(appointmentRepository
            .findAllByPatient(patient)
            .stream()
            .map(element -> modelMapper.map(element, AppointmentResponseDto.class))
            .toList()).thenReturn(List.of());
    
        Mockito.when(appointmentRepository
            .findAllByDoctorUuid(doctorUuid)
            .stream()
            .map(element -> modelMapper.map(element, AppointmentResponseDto.class))
            .toList()).thenReturn(List.of());
    }

    @Test
    public void create_appointment_createWithSuccessfullAppointment(){
        appointmentService.create(request, doctorUuid, patientUuid);
        Mockito.verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    public void create_appointment_notCreateAppointmentIfNotFindDoctor() {}

    @Test
    public void create_appointment_notCreateAppointmentIfNotFindPatient() {}

    @Test
    public void create_appointment_notCreateAppointmentIfHaveSameAppointment() {}

    @Test
    public void find_appointment_findAppointmentByPatientUuid() {}    

    @Test
    public void find_appointment_findAppointmentByDoctorUuid() {
        Mockito.when(appointmentRepository.findAllByDoctorUuid(doctorUuid)).thenReturn(List.of(appointment));
        Mockito.when(modelMapper.map(appointment, AppointmentResponseDto.class)).thenReturn(response);

        List<AppointmentResponseDto> result = appointmentService.findAllByDoctorUuid(doctorUuid);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(response, result.get(0));
    }

    @Test
    public void find_appointment_findAppointmentByPatientUuidNotFindShouldReturnError() {
        UUID invalidPatientUuid = UUID.fromString("11111111-1b46-4bf6-ae56-000000000000");
        Mockito.when(patientRepository.findByUuid(invalidPatientUuid)).thenReturn(Optional.empty());

        Assertions.assertThrows(PatientNotFoundException.class, () -> {
            appointmentService.findAllByPatient(invalidPatientUuid);
        });
    }


    @Test
    public void update_appointment_withSuccess() {
        Mockito.when(appointmentRepository.findByDoctorAndUuid(doctor, appointmentUuid)).thenReturn(Optional.of(appointment));

        appointmentService.update(request, doctorUuid, appointmentUuid);

        Mockito.verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    public void update_appointment_notFindDoctorShouldReturnError() {}
    
    @Test
    public void update_appointment_notFindAppointmentShouldReturnError() {}

    @Test
    public void delete_appointment_notFindDoctorShouldReturnError() {}

    @Test
    public void delete_appointment_notFindAppointmentShouldReturnError() {} 

    @Test
    public void delete_appointment_notChanchingAppointmentStatusToWaiting() {}

    @Test
    public void delete_appointment_mustChanchingAppointmentStatusWithSuccess() {}
}
