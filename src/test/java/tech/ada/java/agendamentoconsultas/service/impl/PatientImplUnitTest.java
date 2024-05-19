package tech.ada.java.agendamentoconsultas.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import tech.ada.java.agendamentoconsultas.exception.CepNotFoundException;
import tech.ada.java.agendamentoconsultas.exception.CpfNotValidException;
import tech.ada.java.agendamentoconsultas.exception.PatientNotFoundException;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressRequestDto;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;
import tech.ada.java.agendamentoconsultas.repository.AddressRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import tech.ada.java.agendamentoconsultas.service.ViaCepService;
import tech.ada.java.agendamentoconsultas.utils.CepUtils;

@ExtendWith(MockitoExtension.class)
public class PatientImplUnitTest {
    @Mock(lenient = true)
    private PatientRepository patientRepository;
    @Mock(lenient = true)
    private ViaCepService viaCepService;
    @Mock(lenient = true)
    private AddressRepository addressRepository;
    @Mock(lenient = true)
    private ModelMapper modelMapper;
    @InjectMocks
    private PatientImpl patientService;

    private PatientDtoRequest patientDto;
    private PatientUpdateRequestDto updateDto;
    private Patient patient;

    @BeforeEach
    public void setup(){
        patientDto = new PatientDtoRequest();
        patientDto.setNome("Unit-Test-valid");
        patientDto.setEmail("test@testando.com");
        patientDto.setSenha("Abcd_1234");
        patientDto.setCpf("015.896.490-01");
        patientDto.setAddressRequestDto(new AddressRequestDto("51250-150", 0));

        patient = new Patient();
        patient.setIsActive(true);

        updateDto = new PatientUpdateRequestDto();
        updateDto.setEmail("test@testando.com");
        updateDto.setNome("Unit-Test-valid");
        updateDto.setTelefone("82999999999");

        Address address = new Address();
        address.setCep("51250-150");

        Mockito.when(addressRepository.findByCep(CepUtils.removeNotNumberCharToCep("51250-150"))).thenReturn(Optional.of(address));
        Mockito.when(addressRepository.findByCep(CepUtils.removeNotNumberCharToCep("12345-678"))).thenReturn(Optional.empty());
        Mockito.when(viaCepService.findAddressByCep("12345-678")).thenThrow(new CepNotFoundException("12345-678"));

        Mockito.when(patientRepository.findByUuid(UUID.fromString("5cc7b9da-0360-472b-b665-bf865862f286"))).thenReturn(Optional.of(patient));
        Mockito.when(patientRepository.findByUuid(UUID.fromString("5cc7b9da-0360-472b-b665-bf865862f281"))).thenReturn(Optional.empty());
    }

    @Test
    public void create_patient_validPatientMustSuccess(){
        patientService.createPatient(patientDto);
        Mockito.verify(patientRepository, Mockito.times(1)).save(any(Patient.class));
    }

    @ParameterizedTest
    @ValueSource(strings = { "11111111111", "22222222222", "12345678901" })
    public void create_patient_patientDocumentNotValidTest(String invalidCpf){
        Assertions.assertThrows(CpfNotValidException.class, () -> {
            patientDto.setCpf(invalidCpf);
        });
        Mockito.verify(patientRepository, Mockito.never()).save(any(Patient.class));
    }

    @Test
    public void create_patient_cepNotFoundShouldNotSave() {
        patientDto.getAddressRequestDto().setCep("12345-678");
        Assertions.assertThrows(CepNotFoundException.class
        , () -> patientService.createPatient(patientDto));
        Mockito.verify(patientRepository, Mockito.never()).save(any(Patient.class));
    }

    @Test
    public void update_patient_updatePatientValidMustSuccess() {
        patientService.update(updateDto, UUID.fromString("5cc7b9da-0360-472b-b665-bf865862f286"));
        Assertions.assertEquals(updateDto.getNome(), patient.getNome());
        Assertions.assertEquals(updateDto.getEmail(), patient.getEmail());
        Assertions.assertEquals(updateDto.getTelefone(), patient.getTelefone());
        Assertions.assertEquals(LocalDate.now(), patient.getUpdateAt().toLocalDate());
        Mockito.verify(patientRepository,Mockito.times(1)).save(any(Patient.class));
    }

    @Test
    public void update_patient_updatePatientNotFoundMustNotSuccess() {
        Assertions.assertThrows(PatientNotFoundException.class, () -> {
            patientService.update(updateDto, UUID.fromString("5cc7b9da-0360-472b-b665-bf865862f281"));
        });
        Mockito.verify(patientRepository,Mockito.never()).save(any(Patient.class));
    }

    @Test
    public void delete_patient_deletePatientFoundMustSuccess() {
        patientService.deletePatient(UUID.fromString("5cc7b9da-0360-472b-b665-bf865862f286"));
        Assertions.assertEquals(false, patient.getIsActive());
        Assertions.assertEquals(LocalDate.now(), patient.getUpdateAt().toLocalDate());
    }

    @Test
    public void delete_patient_PatientNotFoundMustNotSuccess() {
        Assertions.assertThrows(PatientNotFoundException.class, () -> {
            patientService.deletePatient(UUID.fromString("5cc7b9da-0360-472b-b665-bf865862f281"));
        });
        Mockito.verify(patientRepository,Mockito.never()).save(any(Patient.class));
    }
}
