package tech.ada.java.agendamentoconsultas.utils;

public class CepUtils {
    public static String removeNotNumberCharToCep(String cep){
        return cep.replaceAll("[^0-9]", "");
    }
    public static Boolean isValidCep(String cep){
        return removeNotNumberCharToCep(cep).length() == 8;
    }
}
