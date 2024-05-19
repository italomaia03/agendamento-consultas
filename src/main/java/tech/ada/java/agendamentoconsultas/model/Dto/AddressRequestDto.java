package tech.ada.java.agendamentoconsultas.model.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP inválido")
    @NotBlank(message = "O campo CEP deve ser preenchido")
    private String cep;
    private Integer numero;
}