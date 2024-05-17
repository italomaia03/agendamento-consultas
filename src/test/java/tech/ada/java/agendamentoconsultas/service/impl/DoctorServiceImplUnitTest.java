package tech.ada.java.agendamentoconsultas.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.ada.java.agendamentoconsultas.exception.DoctorNotFoundException;
import tech.ada.java.agendamentoconsultas.exception.InvalidCepException;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoResponse;
import tech.ada.java.agendamentoconsultas.repository.AddressRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.service.ViaCepService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplUnitTest {
    @Mock
    private DoctorRepository repository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ViaCepService viaCepService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private DoctorServiceImpl sut;

    private DoctorDtoRequest doctorDtoRequest;
    private AddressRequestDto addressRequestDto;
    private Doctor doctor;
    private Address addressFromDb;
    private AddressDto addressDto;


    @BeforeEach
    public void setUp() {
        addressRequestDto = new AddressRequestDto("63500301", 123);
        addressDto = new AddressDto();
        addressDto.setCep("unit-test");
        addressFromDb = new Address();
        doctor = new Doctor();
        doctorDtoRequest = new DoctorDtoRequest();
        doctorDtoRequest.setAddress(addressRequestDto);

    }

    @Test
    public void createDoctor_withAddressFromDb_shouldBeSuccess() {
        Mockito.when(addressRepository
                .findByCepAndNumero(doctorDtoRequest.getAddress().getCep(),doctorDtoRequest.getAddress().getNumero()))
                .thenReturn(Optional.of(addressFromDb));

        Mockito.when(modelMapper.map(doctorDtoRequest, Doctor.class))
                .thenReturn(doctor);

        sut.addDoctor(doctorDtoRequest);
        Mockito.verify(addressRepository, Mockito.times(1))
                .findByCepAndNumero(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void createDoctor_withAddressFromExtApi_shouldBeSuccess() {
        addressRequestDto.setCep("63500-255");
        Mockito.when(viaCepService
                        .findAddressByCep(doctorDtoRequest.getAddress().getCep()))
                .thenReturn(addressDto);

        Mockito.when(modelMapper.map(doctorDtoRequest, Doctor.class))
                .thenReturn(doctor);

        sut.addDoctor(doctorDtoRequest);
        Mockito.verify(viaCepService, Mockito.times(1)).findAddressByCep(Mockito.anyString());
    }

    @Test
    public void createDoctor_withInvalidAddressFromExtApi_mustFail() {
        addressRequestDto.setCep("invalid");
        Mockito.when(viaCepService.findAddressByCep(Mockito.anyString())).thenThrow(new InvalidCepException());

        Mockito.when(modelMapper.map(doctorDtoRequest, Doctor.class))
                .thenReturn(doctor);

        Assertions.assertThrows(InvalidCepException.class, () -> sut.addDoctor(doctorDtoRequest));

        Mockito.verify(viaCepService, Mockito.times(1)).findAddressByCep(Mockito.anyString());
    }

    @Test
    public void findAllDoctors_shouldBeSuccess() {
        Mockito.when(repository.findAll()).thenReturn(List.of(doctor));

        List<DoctorDtoResponse> results = sut.findALl();

        Assertions.assertEquals(1, results.size());
    }

    @Test
    public void findDoctorByUuId_WithValidUuid_shouldBeSuccess() {
        UUID uuid = UUID.randomUUID();

        doctor.setUuid(uuid);

        DoctorDtoResponse doctorDtoResponse = new DoctorDtoResponse();

        doctorDtoResponse.setUuid(uuid);

        Mockito.when(repository.findByUuid(uuid)).thenReturn(Optional.of(doctor));

        Mockito.when(modelMapper.map(doctor, DoctorDtoResponse.class))
                .thenReturn(doctorDtoResponse);

        DoctorDtoResponse results = sut.findByUuid(uuid);

        Assertions.assertEquals(uuid, results.getUuid());
    }

    @Test
    public void findDoctorByUuId_WithInvalidUuid_shouldBeFail() {
        UUID uuid = UUID.randomUUID();

        Mockito.when(repository.findByUuid(uuid)).thenThrow(DoctorNotFoundException.class);

        Assertions.assertThrows(DoctorNotFoundException.class, () -> sut.findByUuid(uuid));
    }

    @Test
    public void updateDoctor_WithValidParameters_shouldBeSuccess() {

        UUID uuid = UUID.randomUUID();

        doctor.setUuid(uuid);

        doctor.setPassword("password");

        Mockito.when(addressRepository
                        .findByCepAndNumero(doctorDtoRequest.getAddress().getCep(),doctorDtoRequest.getAddress().getNumero()))
                .thenReturn(Optional.of(addressFromDb));

        Mockito.when(repository.findByUuid(uuid)).thenReturn(Optional.of(doctor));

        Mockito.when(modelMapper.map(doctorDtoRequest, Doctor.class))
                .thenReturn(doctor);

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("teste");

        sut.update(uuid, doctorDtoRequest);
    }

    @Test
    public void updateDoctor_WithInvalidUuid_shouldBeFail() {
        UUID uuid = UUID.randomUUID();

        Mockito.when(repository.findByUuid(uuid)).thenThrow(DoctorNotFoundException.class);

        Assertions.assertThrows(DoctorNotFoundException.class, () -> sut.update(uuid, doctorDtoRequest));

        Mockito.verify(repository,Mockito.never()).save(Mockito.any(Doctor.class));
    }

    @Test
    public void deleteDoctor_WithValidParameters_shouldBeSuccess() {
        UUID uuid = UUID.randomUUID();
        doctor.setUuid(uuid);

        Mockito.when(repository.existsByUuid(uuid)).thenReturn(true);

        sut.delete(uuid);
    }

    @Test
    public void deleteDoctor_WithInvalidUuid_shouldBeFail() {
        UUID uuid = UUID.randomUUID();

        Mockito.when(repository.existsByUuid(uuid)).thenReturn(false);

        Assertions.assertThrows(DoctorNotFoundException.class, () -> sut.delete(uuid));

        Mockito.verify(repository,Mockito.never()).deleteByUuid(Mockito.any());
    }
}
