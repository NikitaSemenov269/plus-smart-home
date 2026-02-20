package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.order.CreateNewOrderRequest;
import ru.yandex.practicum.DTO.order.OrderDto;
import ru.yandex.practicum.DTO.order.ProductReturnRequest;
import ru.yandex.practicum.DTO.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.api.ShoppingCartApi;
import ru.yandex.practicum.api.ShoppingStoreApi;
import ru.yandex.practicum.api.WarehouseApi;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.exception.order.IncorrectNumberOfReturnedItemsException;
import ru.yandex.practicum.exception.order.OrderNotFoundException;
import ru.yandex.practicum.exception.shoppingStore.ProductNotFoundException;
import ru.yandex.practicum.interfaces.OrderRepository;
import ru.yandex.practicum.interfaces.OrderService;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper mapper;
    private final OrderRepository repository;
    private final ShoppingCartApi shoppingCartApi;
    private final ShoppingStoreApi shoppingStoreApi;
    private final WarehouseApi warehouseApi;

    @Override
    @Transactional
    public OrderDto createNewOrder(CreateNewOrderRequest dto) {
        log.info("FFFFFFFFFFFFFFFFFFFFFFFFF");
        BookedProductsDto productsDto = warehouseApi.checkQuantityOfGoodsInStock(dto.getShoppingCart());

        // Dto сервиса оплаты со всеми вытекающими исключениями и проверками

        Order newOrder = mapper.toOrder(dto);
        // id корзины и список товаров получены при маппинге.
        // Статус заказа устанавливается дефолтно на NEW
        Order.builder()
                /*      .paymentId()
                      .deliveryId()
                    !_Получаем из DTO сервиса оплаты
                      .totalPrice()
                      .deliveryPrice()
                      .productPrice()*/
                .fragile(productsDto.getFragile())
                .deliveryWeight(productsDto.getDeliveryWeight())
                .deliveryVolume(productsDto.getDeliveryVolume())
                .build();

        repository.save(newOrder);
        log.info("FFFFFFFFFFFFFFFFFFFFFFFFF");
        return mapper.toDto(newOrder);

        // Логика остатков товаров требует доработки
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrdersOfUser(String username, Pageable pageable) {
        log.info("");
        return repository.findAllByUsername(username, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto orderRefund(ProductReturnRequest productReturnRequest) {
        Map<UUID, Long> productsReturn = productReturnRequest.getProducts();

        if (productsReturn.isEmpty()) {
            throw new ProductNotFoundException("FFFFFFFFFFFFFFFFFFFFFF");
        }

        Order order = repository.findById(productReturnRequest.getOrderId()).orElseThrow(
                () -> new OrderNotFoundException("FFFFFF")
        );

        Map<UUID, Long> failure = new HashMap<>();
        Map<UUID, Long> newOrder = new HashMap<>(order.getProducts());

        for (UUID key : productsReturn.keySet()) {
            if (order.getProducts().get(key) < productsReturn.get(key)) {
                failure.put(key, productsReturn.get(key) - order.getProducts().get(key));
            } else if (failure.isEmpty()) {
                newOrder.replace(key, order.getProducts().get(key) - productsReturn.get(key));
            }
        }

        if (!failure.isEmpty()) {
            throw new IncorrectNumberOfReturnedItemsException("Зафиксирована попытка вернуть большее количество" +
                    " товаров. Превышение у ID: " + failure.keySet() + "в количестве: " + failure.values() + " соответственно.");
        }

        order.setProducts(newOrder);

        // Продукт возвращается на склад, поэтому проверка достаточности не требуется.
        warehouseApi.increaseProductQuantity(AddProductToWarehouseRequest.builder()
                        .products(productsReturn).build(),
                OrderState.PRODUCT_RETURNED);

        return mapper.toDto(order);
    }


}
