package ru.yandex.practicum.DTO.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private UUID paymentId;

    private UUID orderId;

    @NotNull
    @Min(0)
    private BigDecimal productsPrice;

    @NotNull
    @Min(0)
    private BigDecimal totalPrice;

    @NotNull
    @Min(0)
    private BigDecimal tax;

}

