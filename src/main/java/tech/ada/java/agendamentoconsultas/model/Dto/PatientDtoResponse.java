package tech.ada.java.agendamentoconsultas.model.Dto;

import java.io.Serializable;
import java.util.UUID;

public class PatientDtoResponse implements Serializable{
    private String nome;
    private String email;
    private UUID uuid;
    public PatientDtoResponse(String nome, String email, UUID uuid) {
        this.nome = nome;
        this.email = email;
        this.uuid = uuid;
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
    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    
}
