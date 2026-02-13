package ru.yandex.practicum.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;

import java.util.Set;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartApi {

    @PutMapping
    ShoppingCartDto addProductsAtShoppingCart(@RequestParam String username,
                                              @RequestBody ShoppingCartDto shoppingCartDto);

    @GetMapping
    ShoppingCartDto getShoppingCartOfUser(@RequestParam String username);

    @DeleteMapping
    void deactivatingTheShoppingCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto deleteItemsFromShoppingCart(@RequestParam String username,
                                                @RequestParam Set<UUID> productIds);

    @PostMapping("change-quantity")
    ShoppingCartDto changeNumberOfItemsInTheBasket(
            @RequestParam
            String username,
            @RequestBody ChangeProductQuantityRequest changeQuantity);
}
