package tech.ada.java.agendamentoconsultas.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.ada.java.agendamentoconsultas.model.Dto.AddressDto;
import tech.ada.java.agendamentoconsultas.service.AddressService;

@RestController
@RequestMapping("/api/v1")
public class AgendamentoController {
    
    private final AddressService addressService;

    public AgendamentoController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/")
    public AddressDto index() {
        AddressDto teste = addressService.findByCep("57048810");
        return teste;
    }
}

