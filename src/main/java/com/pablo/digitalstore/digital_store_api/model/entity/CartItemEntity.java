package com.pablo.digitalstore.digital_store_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "cart_items")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    private int quantity;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @PrePersist
    public void onAdd() {
        this.addedAt = LocalDateTime.now();
    }
}
