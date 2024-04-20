package tech.ada.java.agendamentoconsultas.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface AddressService {
    @GetMapping(value = "/{cep}/json/")
    AddressDto findAddressByCep(@PathVariable String cep);
}
