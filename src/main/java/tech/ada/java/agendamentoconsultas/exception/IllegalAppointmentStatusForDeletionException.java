package tech.ada.java.agendamentoconsultas.exception;

public class IllegalAppointmentStatusForDeletionException extends BadRequestException {
    public IllegalAppointmentStatusForDeletionException() {
        super("Status inválido.\n" +
                "Para deletar, informe Status=RESOLVED ou Status=CANCELLED.");
    }
}
