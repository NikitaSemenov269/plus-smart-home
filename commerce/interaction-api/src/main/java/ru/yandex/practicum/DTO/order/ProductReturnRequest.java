package ru.yandex.practicum.DTO.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReturnRequest {
    @NotNull
    private UUID orderId;

    @NotNull
    private Map<UUID, Long> products;

}
