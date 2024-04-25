package tech.ada.java.agendamentoconsultas.model.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
    private LocalTime appointmentStartTime;
    private String appointmentDescription;
}