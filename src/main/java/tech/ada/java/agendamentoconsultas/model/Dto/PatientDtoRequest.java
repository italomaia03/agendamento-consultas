package tech.ada.java.agendamentoconsultas.model.Dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import tech.ada.java.agendamentoconsultas.utils.DocumentUtils;

public class PatientDtoRequest implements Serializable{
    @Pattern(regexp = "^[\\p{L}]+$", message = "O nome deve conter apenas caracteres alfabéticos.")
    private String nome;
    @Email(message = "Coloque um email em um formato válido(ex: usuario@dominio.com")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[_*#@]).{8,32}$",
    message = "A senha deve conter de 8 a 20 caracteres (lowercase, uppercase, numbers, special(_,*,#,@))")
    private String senha;
    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s?(\\d{4,5})-?(\\d{4})$", message = "O telefone deve estar no formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX.")
    private String telefone;
    private String cpf;
    private AddressRequestDto addressRequestDto;

    public PatientDtoRequest(String nome, String email, String senha, String telefone, String cpf, AddressRequestDto addressRequestDto) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.setCpf(cpf);
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
        if(DocumentUtils.cpfIsValid(cpf)){
            this.cpf = cpf;
        }
        throw new RuntimeException("O cpf fornecido está inválido");
    }

    public AddressRequestDto getAddressRequestDto() {
        return addressRequestDto;
    }

    public void setAddressRequestDto(AddressRequestDto addressRequestDto) {
        this.addressRequestDto = addressRequestDto;
    }

    
}
