package com.pablo.digitalstore.digital_store_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user_products", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}))
public class UserProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProductId;

    @ManyToOne(optional = false)
    private UserEntity user;

    @ManyToOne(optional = false)
    private ProductEntity product;

    private LocalDateTime purchaseDate;

}

