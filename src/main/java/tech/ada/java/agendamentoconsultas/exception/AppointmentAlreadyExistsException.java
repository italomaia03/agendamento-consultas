package tech.ada.java.agendamentoconsultas.exception;

public class AppointmentAlreadyExistsException extends BadRequestException {
    public AppointmentAlreadyExistsException() {
        super("Horário já preenchido, tente novamente com outra data ou outro horário.");
    }
}
