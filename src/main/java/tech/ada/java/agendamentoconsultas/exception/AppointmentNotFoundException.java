package tech.ada.java.agendamentoconsultas.exception;

public class AppointmentNotFoundException extends NotFoundException{
    public AppointmentNotFoundException() {
        super("O agendamento de consulta solicitado não foi encontrado");
    }
}
