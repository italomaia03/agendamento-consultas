package tech.ada.java.agendamentoconsultas.exception;

public class InvalidCepException extends BadRequestException {
    public InvalidCepException() {
        super("CEP inválido");
    }
}
