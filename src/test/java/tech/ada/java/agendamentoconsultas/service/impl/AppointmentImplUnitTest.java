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
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentDeleteRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;
import tech.ada.java.agendamentoconsultas.model.enums.AppointmentStatus;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.AppointmentRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Case 01 - Cria uma marcação com sucesso;
// Case 02 - Cria uma marcação sem encontrar o médico não deve criar;
// Case 03 - Cria uma marcação sem encontrar o paciente não deve criar;
// Case 04 - Cria uma marcação que já existe não deve criar;
// Case 05 - Encontra uma marcação pelo uuid do paciente;
// Case 06 - Encontra uma marcação pelo uuid do médico;
// Case 07 - Não encontra o paciente deve retornar um erro;
// Case 08 - Deve fazer um update da marcação com sucesso;
// Case 09 - Não deve fazer o update se o médico não for encontrado;
// Case 10 - Não deve fazer o update se a marcação não for encontrada;
// Case 11 - Não deve apagar uma consulta de um médico nao encontrado;
// Case 12 - Não deve apagar uma consulta não encontrada;
// Case 13 - Não deve receber uma requisição com o status de WAITING;
// Case 14 - Delete de uma consulta deve modificar o status para RESOLVED ou CANCELLED.

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
        Mockito.when(appointmentRepository.findByDoctorAndUuid(doctor, any(UUID.class))).thenReturn(Optional.empty());

        Mockito.when(doctorRepository.findByUuid(doctorUuid)).thenReturn(Optional.of(doctor));
        Mockito.when(doctorRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());
        
        Mockito.when(patientRepository.findByUuid(patientUuid)).thenReturn(Optional.of(patient));
        Mockito.when(patientRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());
    
        Mockito.when(appointmentRepository.appointmentExists(
            request.getAppointmentDate(), Mockito.eq(doctorUuid), request.getAppointmentStartTime())).thenReturn(false);
        Mockito.when(appointmentRepository.appointmentExists(
            Mockito.any(), Mockito.eq(doctorUuid), Mockito.any())).thenReturn(false);
        
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
        Mockito.verify(appointmentRepository, Mockito.times(1)).save(appointment);
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
    public void find_appointment_findAppointmentByDoctorUuid() {}   

    @Test
    public void find_appointment_findAppointmentByPatientUuidNotFindShouldReturnError() {}   

    @Test
    public void update_appointment_withSuccess() {}   

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
