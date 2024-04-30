package tech.ada.java.agendamentoconsultas.model.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto implements Serializable {
    @NotNull
    @FutureOrPresent(message = "Informe uma data válida a partir da data atual")
    private LocalDate appointmentDate;
    @NotNull
    @FutureOrPresent(message = "Informe um horário válido")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(implementation = String.class)
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):0{2}$")
    private LocalTime appointmentStartTime;
    @NotBlank(message = "Informe uma descrição para a consulta")
    private String appointmentDescription;
}