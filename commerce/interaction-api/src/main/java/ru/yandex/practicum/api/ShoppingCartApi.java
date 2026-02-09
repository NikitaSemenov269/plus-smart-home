package ru.yandex.practicum.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartApi {

    @PutMapping
    ShoppingCartDto addProductsAtShoppingCart(@RequestParam @NotBlank String username,
                                              @Valid @RequestBody ShoppingCartDto shoppingCartDto);
    @GetMapping
    ShoppingCartDto getShoppingCartOfUser(@RequestParam
                                          @NotBlank String username);


}
