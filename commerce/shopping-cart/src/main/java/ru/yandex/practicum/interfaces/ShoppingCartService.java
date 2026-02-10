package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCart createNewCart(String username, Map<UUID, Integer> newProducts);

    ShoppingCartDto getShoppingCartOfUser(String username);

    ShoppingCartDto addProductsAtShoppingCart(String username, Map<UUID, Integer> products);

    void deactivatingTheShoppingCart(String username);

    ShoppingCartDto deleteItemsFromShoppingCart(String username, Set<UUID> productIds);

    ShoppingCartDto changeNumberOfItemsInTheBasket(String username, ChangeProductQuantityRequest changeQuantity);
}
