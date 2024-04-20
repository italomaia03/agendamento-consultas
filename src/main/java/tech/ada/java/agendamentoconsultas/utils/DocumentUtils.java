package tech.ada.java.agendamentoconsultas.utils;

public class DocumentUtils {
    public static Boolean cpfIsValid(String cpf){
        String document = cpf;
        cpf = document.replaceAll("[^0-9]", "");

        if(cpf.length() != 11) return false;
        if(cpf.matches("(\\d)\\1*")) return false;

        int sum = 0;

        for (int i = 0; i < 9; i++) {
            sum += (10 - i) * (cpf.charAt(i) - '0');
        }
        int digit1 = 11 - (sum % 11);
        if (digit1 > 9) digit1 = 0;

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (11 - i) * (cpf.charAt(i) - '0');
        }
        int digit2 = 11 - (sum % 11);
        if (digit2 > 9) digit2 = 0;

        return (cpf.charAt(9) - '0') == digit1 && (cpf.charAt(10) - '0') == digit2;
    }
}
