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
import tech.ada.java.agendamentoconsultas.exception.InvalidCepException;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import tech.ada.java.agendamentoconsultas.repository.AddressRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.service.ViaCepService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplCreateDoctorUnitTest {
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
        Mockito.when(modelMapper.map(doctorDtoRequest, Doctor.class))
                .thenReturn(doctor);
    }

    @Test
    public void createDoctor_withAddressFromDb_shouldBeSuccess() {
        Mockito.when(addressRepository
                .findByCepAndNumero(doctorDtoRequest.getAddress().getCep(),doctorDtoRequest.getAddress().getNumero()))
                .thenReturn(Optional.of(addressFromDb));

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

        sut.addDoctor(doctorDtoRequest);
        Mockito.verify(viaCepService, Mockito.times(1)).findAddressByCep(Mockito.anyString());
    }

    @Test
    public void createDoctor_withInvalidAddressFromExtApi_mustFail() {
        addressRequestDto.setCep("invalid");
        Mockito.when(viaCepService.findAddressByCep(Mockito.anyString())).thenThrow(new InvalidCepException());

        Assertions.assertThrows(InvalidCepException.class, () -> sut.addDoctor(doctorDtoRequest));

        Mockito.verify(viaCepService, Mockito.times(1)).findAddressByCep(Mockito.anyString());
    }
}
