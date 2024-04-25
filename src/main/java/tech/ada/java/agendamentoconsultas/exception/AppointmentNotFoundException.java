package tech.ada.java.agendamentoconsultas.exception;

public class AppointmentNotFoundException extends NotFoundException{
    public AppointmentNotFoundException() {
        super("Não foi encontrado agendamento para o paciente ou médico informados");
    }
}
