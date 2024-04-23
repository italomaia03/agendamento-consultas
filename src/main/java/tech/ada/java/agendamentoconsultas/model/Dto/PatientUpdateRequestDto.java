package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PatientUpdateRequestDto {
    private String nome;
    private String email;
    private String telefone;
}
