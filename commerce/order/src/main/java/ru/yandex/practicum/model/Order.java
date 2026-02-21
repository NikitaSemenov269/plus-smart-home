package ru.yandex.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.enums.order.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    /*@Column(name = "username", updatable = false, nullable = false)
    private String username;*/

    @Column(name = "cart_id", nullable = false)
    private UUID shoppingCartId;

    @NotNull
    @ElementCollection
    @CollectionTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false)
    @Builder.Default
    private OrderState state = OrderState.NEW;

    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    @Column(name = "fragile", nullable = false)
    @Builder.Default
    private Boolean fragile = true;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name = "product_price")
    private BigDecimal productPrice;
}
