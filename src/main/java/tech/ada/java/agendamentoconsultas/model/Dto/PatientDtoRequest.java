package tech.ada.java.agendamentoconsultas.model.Dto;

import java.io.Serializable;

public class PatientDtoRequest implements Serializable{
    
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cpf;
    private AddressRequestDto addressRequestDto;

    public PatientDtoRequest(String nome, String email, String senha, String telefone, String cpf, AddressRequestDto addressRequestDto) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.cpf = cpf;
        this.addressRequestDto = addressRequestDto;
    }

    public PatientDtoRequest() {
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
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public AddressRequestDto getAddressRequestDto() {
        return addressRequestDto;
    }

    public void setAddressRequestDto(AddressRequestDto addressRequestDto) {
        this.addressRequestDto = addressRequestDto;
    }

    
}
