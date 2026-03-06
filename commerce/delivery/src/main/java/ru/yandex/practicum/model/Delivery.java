package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.enums.delivery.DeliveryState;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id", nullable = false, updatable = false)
    private UUID deliveryId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "warehouse_country")),
            @AttributeOverride(name = "city", column = @Column(name = "warehouse_city")),
            @AttributeOverride(name = "street", column = @Column(name = "warehouse_street")),
            @AttributeOverride(name = "house", column = @Column(name = "warehouse_house")),
            @AttributeOverride(name = "flat", column = @Column(name = "warehouse_flat"))
    })
    private Address addressOfWarehouse;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "client_country")),
            @AttributeOverride(name = "city", column = @Column(name = "client_city")),
            @AttributeOverride(name = "street", column = @Column(name = "client_street")),
            @AttributeOverride(name = "house", column = @Column(name = "client_house")),
            @AttributeOverride(name = "flat", column = @Column(name = "client_flat"))
    })
    private Address addressOfClient;

    @Embedded
    private BookedProducts bookedProducts;

    @Column(name = "shipping_cost")
    private BigDecimal shippingCost;

    @Column(name = "delivery_state")
    private DeliveryState deliveryState;
}
