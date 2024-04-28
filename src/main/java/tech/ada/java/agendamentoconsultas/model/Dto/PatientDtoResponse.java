package tech.ada.java.agendamentoconsultas.model.Dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientDtoResponse implements Serializable{
    private String nome;
    private String email;
    private UUID uuid;
}
