package tech.ada.java.agendamentoconsultas.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.ada.java.agendamentoconsultas.model.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByKey(String key);
}
