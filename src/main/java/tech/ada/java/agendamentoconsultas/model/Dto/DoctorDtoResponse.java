package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDtoResponse implements Serializable {
    String name;
    Integer crm;
    Boolean isActive;
    String specialty;
    UUID uuid;
}