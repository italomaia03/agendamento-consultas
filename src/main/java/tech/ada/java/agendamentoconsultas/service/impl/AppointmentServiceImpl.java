package tech.ada.java.agendamentoconsultas.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tech.ada.java.agendamentoconsultas.exception.AppointmentAlreadyExistsException;
import tech.ada.java.agendamentoconsultas.exception.DoctorNotFoundException;
import tech.ada.java.agendamentoconsultas.exception.PatientNotFoundException;
import tech.ada.java.agendamentoconsultas.model.Appointment;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AppointmentResponseDto;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.AppointmentRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import tech.ada.java.agendamentoconsultas.service.AppointmentService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public AppointmentResponseDto create(AppointmentRequestDto request, UUID doctorUuid, UUID patientUuid) {
        Doctor doctor = doctorRepository.findByUuid(doctorUuid).orElseThrow(DoctorNotFoundException::new);
        Patient patient = patientRepository.findByUuid(patientUuid).orElseThrow(PatientNotFoundException::new);
        if (appointmentRepository.appointmentExists(request.getAppointmentDate(), doctorUuid, request.getAppointmentStartTime())) {
            throw new AppointmentAlreadyExistsException();
        }
        Appointment appointment = modelMapper.map(request, Appointment.class);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentEndTime(request.getAppointmentStartTime().plusHours(1L));
        return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
    }
}
