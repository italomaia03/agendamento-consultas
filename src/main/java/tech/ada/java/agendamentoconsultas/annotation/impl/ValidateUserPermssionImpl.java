package tech.ada.java.agendamentoconsultas.annotation.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tech.ada.java.agendamentoconsultas.model.AuthenticatedUser;

import java.util.UUID;

@Aspect
@Component
public class ValidateUserPermssionImpl {

    @Around("@annotation(tech.ada.java.agendamentoconsultas.annotation.ValidateUserPermission)")
    public Object validateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        var authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var pathVariable = (UUID) joinPoint.getArgs()[0];
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authUser;
        validateAuthenticatedUser(pathVariable, authenticatedUser);
        return joinPoint.proceed();
    }

    private void validateAuthenticatedUser(UUID pathUuid, AuthenticatedUser user) throws Throwable {
        if (!user.getUuid().equals(pathUuid) && user.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Acesso negado");
        }
    }
}
