package tech.ada.java.agendamentoconsultas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.ada.java.agendamentoconsultas.model.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{
    
}
