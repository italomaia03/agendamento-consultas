package tech.ada.java.agendamentoconsultas.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import tech.ada.java.agendamentoconsultas.exception.CepNotFoundException;
import tech.ada.java.agendamentoconsultas.exception.PatientNotFoundException;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Dto.*;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.AddressRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;
import tech.ada.java.agendamentoconsultas.service.PatientService;
import tech.ada.java.agendamentoconsultas.service.ViaCepService;
import tech.ada.java.agendamentoconsultas.utils.CepUtils;
import tech.ada.java.agendamentoconsultas.utils.DocumentUtils;

@Service
public class PatientImpl implements PatientService{
    private final PatientRepository patientRepository;
    private final ViaCepService viaCepService;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public PatientImpl(PatientRepository patientRepository, ModelMapper modelMapper,ViaCepService viaCepService, AddressRepository addressRepository){
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.viaCepService = viaCepService;
    }



    @Override
    public PatientDtoResponse createPatient(PatientDtoRequest request) {
        
        Optional<Address> optionalAddress = addressRepository.findByCep(CepUtils.removeNotNumberCharToCep(request.getAddressRequestDto().getCep()));
        Address address = new Address();

        try {
            if(optionalAddress.isPresent()){
                address = optionalAddress.get();
            }else{
                address = modelMapper.map(viaCepService.findAddressByCep(request.getAddressRequestDto().getCep()), Address.class);
                address.setCep(CepUtils.removeNotNumberCharToCep(address.getCep()));
                addressRepository.save(address);
            }
        } catch (RuntimeException e) {
            throw new CepNotFoundException(request.getAddressRequestDto().getCep());
        }

        Patient patient = new Patient(request.getNome(), request.getEmail(), request.getSenha(), request.getTelefone(), request.getCpf(), address);
        patientRepository.save(patient);
        
        return new PatientDtoResponse(request.getNome(), request.getEmail(), patient.getUuid());
    }

    @Override
    public void update(PatientUpdateRequestDto request, UUID uuid) {
        
        Patient paciente = patientRepository.findByUuid(uuid).orElseThrow(PatientNotFoundException::new);

        paciente.setTelefone(request.getTelefone());
        paciente.setNome(request.getNome());
        paciente.setEmail(request.getEmail());

        patientRepository.save(paciente);
    }

    @Override
    public void changePassword(PatientUpdatePasswordDto request, UUID uuid) {

        Patient paciente = patientRepository.findByUuid(uuid).orElseThrow(PatientNotFoundException::new);
        if(paciente.getSenha().equals(request.getSenha())){
            if(request.getNovaSenha().equals(request.getConfirmacaoSenha())){
                paciente.setSenha(request.getNovaSenha());
                patientRepository.save(paciente);
            } else {
                throw new RuntimeException("A confirmação precisa ser igual a nova senha.");
            }
        } else {
            throw new RuntimeException("Senha incorreta.");
        }
    }

    @Override
    public List<PatientGetAllResponseDto> getAll() {
        return patientRepository.findAll()
                .stream().map(elemento -> this.modelMapper.map(elemento, PatientGetAllResponseDto.class)).toList();
    }

}
