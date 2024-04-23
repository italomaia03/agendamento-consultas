package tech.ada.java.agendamentoconsultas.exception;

public class PatientNotFoundException extends NotFoundException{

    public PatientNotFoundException() {
        super("Paciente não encontrado");
    }
    
}
