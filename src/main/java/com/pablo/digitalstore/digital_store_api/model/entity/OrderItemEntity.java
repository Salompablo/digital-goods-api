package com.pablo.digitalstore.digital_store_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    private OrderEntity order;

    @ManyToOne
    private ProductEntity product;

    private Integer quantity;
    private BigDecimal unitPrice;
}
