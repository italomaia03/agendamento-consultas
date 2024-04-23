package tech.ada.java.agendamentoconsultas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.ada.java.agendamentoconsultas.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{
    Optional<Address> findByCep(String cep);
}
