package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientAppointmentResponseDto implements Serializable {
    private String nome;
    private String telefone;
}