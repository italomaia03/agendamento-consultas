package tech.ada.java.agendamentoconsultas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.ada.java.agendamentoconsultas.model.enums.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin implements UserDetails, AuthenticatedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String email;
    private String password;
    private Boolean isActive = true;
    private UUID uuid;
    private Boolean accountExpired = !isActive;
    private Boolean credentialsExpired = !isActive;
    private Boolean accountLocked = !isActive;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ADMIN;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()), new SimpleGrantedAuthority("ROLE_" + UserRole.DOCTOR.name()), new SimpleGrantedAuthority("ROLE_" + UserRole.PATIENT.name()));
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