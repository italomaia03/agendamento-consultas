package tech.ada.java.agendamentoconsultas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    private Integer crm;

    @Column(columnDefinition = "boolean DEFAULT true", insertable = false)
    private Boolean isActive;

    private String specialty;

    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", insertable = false)
    private UUID uuid;
}