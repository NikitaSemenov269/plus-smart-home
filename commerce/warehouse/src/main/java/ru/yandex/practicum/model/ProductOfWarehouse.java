package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouse_products")
public class ProductOfWarehouse {

    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId; // ID товара из каталога
    @Embedded
    private Dimension dimension;
    @Column(name = "weight", nullable = false)
    private Double weight;
    @Column(name = "fragile", nullable = false)
    private Boolean fragile;
    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
