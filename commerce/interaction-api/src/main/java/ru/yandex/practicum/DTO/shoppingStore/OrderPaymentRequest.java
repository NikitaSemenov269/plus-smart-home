package ru.yandex.practicum.DTO.shoppingStore;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentRequest {
    private UUID productId;

    @Min(value = 1, message = "Минимальная цена должна равняться 1.")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @Min(0)
    @Builder.Default
    private Integer quantity = 0;
}
