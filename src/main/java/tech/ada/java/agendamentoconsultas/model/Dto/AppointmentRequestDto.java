package tech.ada.java.agendamentoconsultas.model.Dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto implements Serializable {
    @NotNull
    @FutureOrPresent(message = "Informe uma data válida a partir da data atual")
    private LocalDate appointmentDate;
    @NotNull
    private LocalTime appointmentStartTime;
    @NotBlank(message = "Informe uma descrição para a consulta")
    private String appointmentDescription;
}