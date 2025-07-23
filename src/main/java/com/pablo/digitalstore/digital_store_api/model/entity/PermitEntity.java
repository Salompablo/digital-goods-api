package com.pablo.digitalstore.digital_store_api.model.entity;

import com.pablo.digitalstore.digital_store_api.model.enums.Permits;
import jakarta.persistence.*;

@Entity
@Table(name = "permits")
public class PermitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permitId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Permits permit;
}
