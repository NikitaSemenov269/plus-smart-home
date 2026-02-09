package ru.yandex.practicum.service;

import feign.FeignException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.DTO.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.api.WarehouseApi;
import ru.yandex.practicum.enums.shoppingCart.CartState;
import ru.yandex.practicum.exception.shoppingCart.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.shoppingCart.NotAuthorizedException;
import ru.yandex.practicum.interfaces.ShoppingCartRepository;
import ru.yandex.practicum.interfaces.ShoppingCartService;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;
    private final WarehouseApi warehouseApi;

    private static final String CART_IS_DEACTIVATE = "Корзина с ID {} находится в статусе 'DEACTIVATE'" +
            " в результате чего нельзя добавлять новые предметы.";

    @Override
    @Transactional
    public ShoppingCart createNewCart(String username, ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart = mapper.toCart(shoppingCartDto);
        shoppingCart.setUsername(username); // Костыль для реализации логики ФЗ
        log.info("Создана новая корзина с ID: {} для пользователя с именем: {}",
                shoppingCart.getShoppingCartId(),
                username);
        repository.save(shoppingCart);
        return shoppingCart;
    }

    // В будущем будет валидация пользователя
    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCartOfUser(String username) {
        return repository.findByUsername(username)
                .map(cart -> {
                            log.info("Найдена корзина c ID: {}", cart.getShoppingCartId());
                            return mapper.toDto(cart);
                        }
                )
                .orElseThrow(() -> new NotAuthorizedException("Корзина для пользователя " + username + " не найдена"));
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductsAtShoppingCart(String username, ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart;
        // Если у корзины, куда добавляются новые товары, нет id - создаем новую корзину.
        if (shoppingCartDto.getShoppingCartId() == null) {
            shoppingCart = createNewCart(username, shoppingCartDto);
            // Иначе ищем корзину по id среди существующих в БД.
        } else {
            shoppingCart = repository.findById(shoppingCartDto.getShoppingCartId())
                    .orElseThrow(() -> new NotFoundException("Корзина c ID " + shoppingCartDto.getShoppingCartId()
                            + " не найдена"));
            // Если найденная корзина имеет статус отличный от DEACTIVATE - добавляем в нее новые товары.
            if (!CartState.DEACTIVATE.equals(shoppingCart.getCartState())) {
                try {
                    warehouseApi.checkQuantityOfGoodsInStock(shoppingCartDto);
                } catch (FeignException e) {
                    log.error("Warehouse check failed: {}", e.getMessage());
                    log.error("Ошибка при проверке наличия товаров на складе: {}", e.getMessage());

                    if (e.status() == 400) {
                        throw new IllegalArgumentException("Товары недоступны в запрашиваемом количестве");
                    }
                    log.warn("Сервис склада временно недоступен. Товары добавлены в корзину без проверки.");
                }
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
                log.info(CART_IS_DEACTIVATE, shoppingCart.getShoppingCartId());
            }
        }
        return mapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public void deactivatingTheShoppingCart(String username) {
        ShoppingCart shoppingCart = findByUsernameOrElseThrow(username);
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
        ShoppingCart shoppingCart = findByUsernameOrElseThrow(username);
        if (!CartState.DEACTIVATE.equals(shoppingCart.getCartState())) {
            Map<UUID, Integer> products = shoppingCart.getProducts();

            productIds.stream()
                    .filter(products::containsKey)
                    .forEach(products::remove);

            log.info("Товары с ID: {} успешно удалены из корзины пользователя {}", productIds, username);
        } else {
            log.info(CART_IS_DEACTIVATE, shoppingCart.getShoppingCartId());
        }
        return mapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeNumberOfItemsInTheBasket(String username, ChangeProductQuantityRequest changeQuantity) {
        ShoppingCart shoppingCart = findByUsernameOrElseThrow(username);

        if (!shoppingCart.getProducts().containsKey(changeQuantity.getProductId())) {
            throw new NoProductsInShoppingCartException("Корзина не содержит изменяемые товары.");
        }
        if (!CartState.DEACTIVATE.equals(shoppingCart.getCartState())) {
            try {
                warehouseApi.checkQuantityOfGoodsInStock(mapper.toDto(shoppingCart));
            } catch (FeignException e) {
                log.error("Warehouse check failed: {}", e.getMessage());
                log.error("Ошибка при проверке наличия товаров на складе: {}", e.getMessage());

                if (e.status() == 400) {
                    throw new IllegalArgumentException("Товары недоступны в запрашиваемом количестве");
                }
                log.warn("Сервис склада временно недоступен. Товары добавлены в корзину без проверки.");
            }
            shoppingCart.getProducts().put(
                    changeQuantity.getProductId(),
                    changeQuantity.getNewQuantity());
            log.info("");
        } else {
            log.info(CART_IS_DEACTIVATE, shoppingCart.getShoppingCartId());
        }
        return mapper.toDto(shoppingCart);
    }

    @Transactional(readOnly = true)
    private ShoppingCart findByUsernameOrElseThrow(String username) {
        log.info("Попытка получить корзину пользователя.");
        return repository.findByUsername(username).orElseThrow(() ->
                new NotAuthorizedException("Корзина пользователя " + username + " не найдена."));
    }
}




