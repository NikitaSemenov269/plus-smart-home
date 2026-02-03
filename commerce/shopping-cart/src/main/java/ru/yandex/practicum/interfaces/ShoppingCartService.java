package ru.yandex.practicum.interfaces;

import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;

import java.util.Set;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartOfUser(UUID shoppingCartId);

    ShoppingCartDto addProductsAtShoppingCart(String username, ShoppingCartDto dto);

    void deactivatingTheShoppingCart(String username);

    ShoppingCartDto deleteItemsFromShoppingCart(String username, Set<UUID> productIds);

}
