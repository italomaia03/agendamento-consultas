package tech.ada.java.agendamentoconsultas.model.Dto;

import java.io.Serializable;

public class PatientDtoResponse implements Serializable{
    private String nome;
    private String email;
    private String cpf;
    public PatientDtoResponse(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }
    public PatientDtoResponse() {
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    
}
