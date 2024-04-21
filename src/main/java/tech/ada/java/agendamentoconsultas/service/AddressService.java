package tech.ada.java.agendamentoconsultas.service;

import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;

public interface AddressService {
    Iterable<AddressDto> findAll();
    AddressDto findByCep(String cep);
}
