package tech.ada.java.agendamentoconsultas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.ada.java.agendamentoconsultas.model.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{
    Optional<Address> findByCepAndNumero(String cep, Integer numero);
}
