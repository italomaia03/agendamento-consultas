package tech.ada.java.agendamentoconsultas.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import tech.ada.java.agendamentoconsultas.exception.PatientNotFoundException;
import tech.ada.java.agendamentoconsultas.model.Address;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientUpdateRequestDto;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoRequest;
import tech.ada.java.agendamentoconsultas.model.Dto.PatientDtoResponse;
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

        if(request.getCpf() == null || request.getCpf().isBlank()) throw new RuntimeException("O cpf do paciente não pode ser vazio");
        if(!DocumentUtils.cpfIsValid(request.getCpf())) throw new RuntimeException("O cpf do paciente não é válido");
        if(!CepUtils.isValidCep(request.getAddressRequestDto().getCep())) throw new RuntimeException("O CEP do paciente não é válido");
        
        Optional<Address> optionalAddress = addressRepository.findByCep(request.getAddressRequestDto().getCep());
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
            throw new RuntimeException("Não foi possível localizar o Cep ou o Cep enviado está inválido");
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
}
