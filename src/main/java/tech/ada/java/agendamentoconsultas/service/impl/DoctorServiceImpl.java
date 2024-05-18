package tech.ada.java.agendamentoconsultas.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.ada.java.agendamentoconsultas.exception.DoctorNotFoundException;
import tech.ada.java.agendamentoconsultas.exception.InvalidCepException;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.DoctorDtoResponse;
import tech.ada.java.agendamentoconsultas.repository.AddressRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.service.DoctorService;
import tech.ada.java.agendamentoconsultas.service.ViaCepService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository repository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final ViaCepService viaCepService;
    private final PasswordEncoder passwordEncoder;

    @Override public List<DoctorDtoResponse> findALl() {

        return this.repository.findAll()
                .stream()
                .map(mapToResponseDto())
                .collect(Collectors.toList());
    }

    @Override public DoctorDtoResponse findByUuid(UUID uuid) {
        return this.repository.findByUuid(uuid)
                .map(mapToResponseDto())
                .orElseThrow(DoctorNotFoundException::new);
    }

    @Override public DoctorDtoResponse addDoctor(DoctorDtoRequest dto) {
        Doctor doctor = this.modelMapper.map(dto, Doctor.class);
        vinculateAddress(dto, doctor);
        return modelMapper.map(repository.save(doctor), DoctorDtoResponse.class);
    }

    private void vinculateAddress(DoctorDtoRequest dto, Doctor doctor) {
        Optional<Address> address = addressRepository.findByCepAndNumero(dto.getAddress().getCep(), dto.getAddress().getNumero());
        if (address.isPresent()) {
            doctor.setAddress(address.get());
        } else {
            try {
                AddressDto addressDto = viaCepService.findAddressByCep(dto.getAddress().getCep());
                formatAddressDto(dto.getAddress().getNumero(), addressDto);
                Address addressEntity = modelMapper.map(addressDto, Address.class);
                doctor.setAddress(addressEntity);
            } catch (RuntimeException e) {
                throw new InvalidCepException();
            }
        }
    }

    private void formatAddressDto(Integer number, AddressDto addressDto) {
        addressDto.setNumero(number);
        String formattedCep = addressDto.getCep().replace("-", "");
        addressDto.setCep(formattedCep);
    }

    @Override public void update(UUID uuid, DoctorDtoRequest newDoctor) {
        Doctor doctor = repository.findByUuid(uuid).orElseThrow(DoctorNotFoundException::new);
        Doctor newDoctorEnt = modelMapper.map(newDoctor, Doctor.class);
        String passwordEncoded = passwordEncoder.encode(newDoctorEnt.getPassword());
        newDoctorEnt.setUuid(doctor.getUuid());
        newDoctorEnt.setId(doctor.getId());
        newDoctorEnt.setPassword(passwordEncoded);
        vinculateAddress(newDoctor, newDoctorEnt);
        repository.save(newDoctorEnt);
    }

    @Override@Transactional
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
