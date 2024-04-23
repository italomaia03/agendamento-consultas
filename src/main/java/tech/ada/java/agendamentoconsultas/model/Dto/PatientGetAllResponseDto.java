package tech.ada.java.agendamentoconsultas.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientGetAllResponseDto implements Serializable {

    private String nome;
    private String email;
    private String telefone;
    private AddressDto address;
}