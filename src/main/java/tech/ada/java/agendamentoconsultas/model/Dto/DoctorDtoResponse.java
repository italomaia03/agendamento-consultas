package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
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