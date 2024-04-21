package tech.ada.java.agendamentoconsultas.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractException {

    protected NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
