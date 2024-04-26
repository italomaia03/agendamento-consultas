package tech.ada.java.agendamentoconsultas.model.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ada.java.agendamentoconsultas.model.AppointmentStatus;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDeleteRequestDto implements Serializable{
    @NotNull
    private AppointmentStatus appointmentStatus;
}