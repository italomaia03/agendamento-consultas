package tech.ada.java.agendamentoconsultas.exception;

public class CpfNotValidException extends BadRequestException{

    public CpfNotValidException() {
        super("CPF adicionado não é válido");
    }
    
}
