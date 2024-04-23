package tech.ada.java.agendamentoconsultas.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractException{

    protected BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
    
}
