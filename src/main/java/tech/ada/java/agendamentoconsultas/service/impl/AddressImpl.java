package tech.ada.java.agendamentoconsultas.service.impl;

import org.springframework.stereotype.Service;

import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;
import tech.ada.java.agendamentoconsultas.service.AddressService;
import tech.ada.java.agendamentoconsultas.service.ViaCepService;

@Service
public class AddressImpl implements AddressService{

    private final ViaCepService viaCepService;

    
    public AddressImpl(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @Override
    public Iterable<AddressDto> findAll() {
        return null;
    }

    @Override
    public AddressDto findByCep(String cep) {
        //Validador cep
        return viaCepService.findAddressByCep(cep);
    }
    
}
