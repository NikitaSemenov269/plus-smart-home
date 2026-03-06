package ru.yandex.practicum.DTO.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeQuantityOfProductToWarehouse {
    @NotNull
    private Map<UUID, Long> products;

}
