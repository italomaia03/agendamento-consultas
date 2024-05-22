package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDtoResponse {
    private String name;
    private String crm;
    private Boolean isActive;
    private String specialty;
    private UUID uuid;
    private AddressResponseDto address;
}