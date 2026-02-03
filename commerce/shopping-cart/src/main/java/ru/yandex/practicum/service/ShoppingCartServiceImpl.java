package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.enums.shoppingCart.CartState;
import ru.yandex.practicum.interfaces.ShoppingCartRepository;
import ru.yandex.practicum.interfaces.ShoppingCartService;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;


    // В будущем будет валидация пользователя
    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCartOfUser(UUID shoppingCartId) {
        return repository.findById(shoppingCartId)
                .map(cart -> {
                            log.info("Найдена корзина c ID: {}", shoppingCartId);
                            return mapper.toDto(cart);
                        }
                )
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductsAtShoppingCart(String username, ShoppingCartDto shoppingCartDto) {
        try {
            ShoppingCart shoppingCart;
            // Если у корзины, куда добавляются новые товары, нет id - создаем новую корзину.
            if (shoppingCartDto.getShoppingCartId() == null) {
                shoppingCart = mapper.toCart(shoppingCartDto);
                shoppingCart.setUsername(username); // Костыль для реализации логики ФЗ
                log.info("Создана новая корзина с ID: {} для пользователя с именем: {}",
                        shoppingCart.getShoppingCartId(),
                        username);
                repository.save(shoppingCart);
            // Иначе ищем корзину по id среди существующих в БД.
            } else {
                shoppingCart = repository.findById(shoppingCartDto.getShoppingCartId())
                        .orElseThrow(NotFoundException::new);
            // Если найденная корзина имеет статус отличный от DEACTIVATE - добавляем в нее новые товары.
                if (!CartState.DEACTIVATE.equals(shoppingCart.getCartState())) {
                    mapper.addOnlyNewProducts(shoppingCartDto, shoppingCart);

                    String idsList = shoppingCart.getProducts().keySet().stream()
                            .map(UUID::toString)
                            .collect(Collectors.joining(", "));

                    if (shoppingCart.getProducts().size() == 1) {
                        log.info("Товар c ID {} успешно добавлен.",
                                idsList);
                    } else {
                        log.info("Товары c ID: {} успешно добавлены.",
                                idsList);
                    }
                // Если статус DEACTIVATE - выводим log и возвращаем корзину без изменений.
                } else {
                    log.info("Корзина с ID {} находится в статусе 'DEACTIVATE' в результате чего нельзя добавлять" +
                            " новые предметы.", shoppingCart.getShoppingCartId());
                }
            }
            return mapper.toDto(shoppingCart);
        } catch (RuntimeException e) {
            log.error("В процессе добавления нового товара возникла ошибка.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deactivatingTheShoppingCart(String username) {
        ShoppingCart shoppingCart = repository.findByUsername(username).orElseThrow(NotFoundException::new);
        if (shoppingCart.getCartState().equals(CartState.DEACTIVATE)) {
            log.info("Корзина пользователя {} уже деактивирована. ID корзины: {}.",
                    username,
                    shoppingCart.getShoppingCartId());
            return;
        }
        shoppingCart.setCartState(CartState.DEACTIVATE);
        log.info("Корзина пользователя {} деактивирована. ID корзины: {}.",
                username,
                shoppingCart.getShoppingCartId());
    }

    @Override
    @Transactional
    public ShoppingCartDto deleteItemsFromShoppingCart(String username, Set<UUID> productIds) {
        ShoppingCart shoppingCart = repository.findByUsername(username).orElseThrow(NotFoundException::new);
        return null;
    }


}




