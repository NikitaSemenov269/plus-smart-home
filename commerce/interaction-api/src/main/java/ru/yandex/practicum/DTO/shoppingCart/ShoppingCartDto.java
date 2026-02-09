package ru.yandex.practicum.DTO.shoppingCart;

import jakarta.validation.constraints.Max;
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

    private Map<
            @NotNull(message = "ID не может равняться null.")
                    UUID,
            @Min(value = 1, message = "Количество товаров не может быть меньше 1.")
            @Max(value = 50, message = "Количество товаров для покупки физ. лицами ограничено 50-ю единицами.")
                    Integer>
            products;
}
