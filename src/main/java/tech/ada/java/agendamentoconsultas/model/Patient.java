package tech.ada.java.agendamentoconsultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.ada.java.agendamentoconsultas.model.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("is_active = true")
public class Patient implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;
    @NotBlank(message = "O nome do usuário é obrigatório.")
    private String nome;
    @NotBlank(message = "O email do usuário é obrigatório.")
    @Email(message = "Coloque um email em um formato válido(ex: usuario@dominio.com")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "O senha do usuário é obrigatória.")
    private String senha;
    @NotBlank(message = "O telefone do usuário é obrigatório.")
    private String telefone;
    @NotBlank(message = "O cpf do usuário é obrigatório.")
    private String cpf;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private Boolean isActive;
    private Boolean accountExpired;
    private Boolean credentialsExpired;
    private Boolean accountLocked;

    @ManyToOne
    @JoinColumn(name = "id_endereco")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Address address;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public Patient(String nome, String email, String senha, String telefone, String cpf, Address address) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.cpf = cpf;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.address = address;
        this.uuid = UUID.randomUUID();
        this.role = UserRole.PATIENT;
        this.isActive = true;
        this.accountExpired = false;
        this.accountLocked = false;
        this.credentialsExpired = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
