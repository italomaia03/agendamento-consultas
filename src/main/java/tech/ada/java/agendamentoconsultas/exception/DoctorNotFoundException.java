package tech.ada.java.agendamentoconsultas.exception;

public class DoctorNotFoundException extends NotFoundException {
    public DoctorNotFoundException() {
        super("Doctor not found");
    }
}
