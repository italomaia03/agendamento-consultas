package tech.ada.java.agendamentoconsultas.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public interface AuthenticatedUser {
    UUID getUuid();
    Collection<? extends GrantedAuthority> getAuthorities();
}
