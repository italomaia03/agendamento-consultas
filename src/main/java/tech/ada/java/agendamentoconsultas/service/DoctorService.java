package tech.ada.java.agendamentoconsultas.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tech.ada.java.agendamentoconsultas.exception.DoctorNotFoundException;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoResponse;
import tech.ada.java.agendamentoconsultas.repository.AddressRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class DoctorService {
    private final DoctorRepository repository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final ViaCepService viaCepService;

    public List<DoctorDtoResponse> findALl() {

        return this.repository.findAll()
                .stream()
                .map(mapToResponseDto())
                .collect(Collectors.toList());
    }

    public DoctorDtoResponse findByUuid(UUID uuid) {
        return this.repository.findByUuid(uuid)
                .map(mapToResponseDto())
                .orElseThrow(DoctorNotFoundException::new);
    }

    public DoctorDtoResponse addDoctor(DoctorDtoRequest dto) {
        Doctor doctor = this.modelMapper.map(dto, Doctor.class);
        vinculateAddress(dto, doctor);
        return modelMapper.map(repository.save(doctor), DoctorDtoResponse.class);
    }

    private void vinculateAddress(DoctorDtoRequest dto, Doctor doctor) {
        Optional<Address> address = addressRepository.findByCepAndNumero(dto.getAddress().getCep(), dto.getAddress().getNumero());
        if (address.isPresent()) {
            doctor.setAddress(address.get());
        } else {
            AddressDto addressDto = viaCepService.findAddressByCep(dto.getAddress().getCep());
            formatAddressDto(dto.getAddress().getNumero(), addressDto);
            Address addressEntity = modelMapper.map(addressDto, Address.class);
            doctor.setAddress(addressEntity);
        }
    }

    private void formatAddressDto(Integer number, AddressDto addressDto) {
        addressDto.setNumero(number);
        String formattedCep = addressDto.getCep().replace("-", "");
        addressDto.setCep(formattedCep);
    }

    public void update(UUID uuid, DoctorDtoRequest newDoctor) {
        Doctor doctor = repository.findByUuid(uuid).orElseThrow(DoctorNotFoundException::new);
        doctor.setName(newDoctor.getName());
        doctor.setCrm(newDoctor.getCrm());
        doctor.setSpecialty(newDoctor.getSpecialty());
        vinculateAddress(newDoctor, doctor);
        repository.save(doctor);
    }

    @Transactional
    public void delete(UUID uuid) {
        if (!repository.existsByUuid(uuid)) {
            throw new DoctorNotFoundException();
        }
        repository.deleteByUuid(uuid);
    }

    private Function<Doctor, DoctorDtoResponse> mapToResponseDto() {
        return doctor -> modelMapper.map(doctor, DoctorDtoResponse.class);
    }
}
