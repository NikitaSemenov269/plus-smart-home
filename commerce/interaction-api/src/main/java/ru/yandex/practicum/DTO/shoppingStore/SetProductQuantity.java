package ru.yandex.practicum.DTO.shoppingStore;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.enums.shoppingStore.QuantityState;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantity {

    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}
