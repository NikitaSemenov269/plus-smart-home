package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.DTO.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.api.ShoppingCartApi;
import ru.yandex.practicum.interfaces.ShoppingCartService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartApi {
    private final ShoppingCartService service;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addProductsAtShoppingCart(@RequestParam
                                                     @NotBlank(message = "Имя пользователя не может быть пустым," +
                                                             " или равняться null.") String username,
                                                     @RequestBody(required = false) Map<UUID, Long> products) {
        log.info("PUT. Добавление продукта в корзину");
        return service.addProductsAtShoppingCart(username, products);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getShoppingCartOfUser(@RequestParam
                                                 @NotBlank(message = "Имя пользователя не может быть пустым," +
                                                         " или равняться null.") String username) {
        log.info("GET. Получение корзины пользователя с username: {}", username);
        return service.getShoppingCartOfUser(username);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivatingTheShoppingCart(@RequestParam
                                            @NotBlank(message = "Имя пользователя не может быть пустым," +
                                                    " или равняться null.") String username) {
        log.info("DELETE. Деактивация корзины пользователя с username: {}", username);
        service.deactivatingTheShoppingCart(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto deleteItemsFromShoppingCart(@RequestParam
                                                       @NotBlank(message = "Имя пользователя не может быть пустым," +
                                                               " или равняться null.") String username,
                                                       @RequestBody
                                                       @NotNull Set<UUID> productIds) {
        log.info("POST. Удаление товара из корзины пользователя: {}", username);
        return service.deleteItemsFromShoppingCart(username, productIds);
    }

    @PostMapping("change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeNumberOfItemsInTheBasket(
            @RequestParam
            @NotBlank(message = "Имя пользователя не может быть пустым, или равняться null.") String username,
            @Valid @RequestBody ChangeProductQuantityRequest changeQuantity) {
        log.info("POST. Изменение количества товара в корзине пользователя: {}", username);
        return service.changeNumberOfItemsInTheBasket(username, changeQuantity);
    }
}
