package ru.yandex.practicum.DTO.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReturnRequest {
    @NotNull
    private UUID orderId;

    @NotNull
    Map<UUID, Long> products;

}
