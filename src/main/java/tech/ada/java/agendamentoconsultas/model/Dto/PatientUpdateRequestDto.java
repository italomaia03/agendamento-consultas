package tech.ada.java.agendamentoconsultas.model.Dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PatientUpdateRequestDto {
    private String nome;
    @Email(message = "Coloque um email em um formato válido(ex: usuario@dominio.com")
    private String email;
    private String telefone;
}
