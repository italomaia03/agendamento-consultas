package tech.ada.java.agendamentoconsultas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_active = true")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String crm;
    private Boolean isActive = true;
    private String specialty;
    private UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "address_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Address address;

}