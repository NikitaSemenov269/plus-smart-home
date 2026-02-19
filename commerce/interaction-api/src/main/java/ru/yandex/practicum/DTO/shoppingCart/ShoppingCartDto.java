package ru.yandex.practicum.DTO.shoppingCart;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {
    private UUID shoppingCartId;
    @NotNull
    private Map<UUID, Integer> products;
}
