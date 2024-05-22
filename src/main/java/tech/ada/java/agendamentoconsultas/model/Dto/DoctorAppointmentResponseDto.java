package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAppointmentResponseDto implements Serializable {
    private String name;
    private String crm;
    private String specialty;
}