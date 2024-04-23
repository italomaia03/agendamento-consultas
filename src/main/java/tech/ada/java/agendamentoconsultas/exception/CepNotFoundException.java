package tech.ada.java.agendamentoconsultas.exception;

public class CepNotFoundException extends NotFoundException{

    public CepNotFoundException(String cep) {
        super("Cep: "+ cep +" não encontrado na base de dados");
    }
    
}
