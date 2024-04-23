package tech.ada.java.agendamentoconsultas.model.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PatientUpdateRequestDto {
    @NotBlank
    @NotNull
    @Pattern(regexp = "^[\\p{L}]+$", message = "O nome deve conter apenas caracteres alfabéticos.")
    private String nome;
    @NotBlank
    @NotNull
    @Email(message = "Coloque um email em um formato válido(ex: usuario@dominio.com")
    private String email;
    @NotBlank
    @NotNull
    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s?(\\d{4,5})-?(\\d{4})$", message = "O telefone deve estar no formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX.")
    private String telefone;
}
