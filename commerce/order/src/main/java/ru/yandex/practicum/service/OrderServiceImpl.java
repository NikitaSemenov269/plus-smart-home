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
import ru.yandex.practicum.DTO.shoppingStore.OrderPaymentRequest;
import ru.yandex.practicum.DTO.shoppingStore.ProductDto;
import ru.yandex.practicum.DTO.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.api.ShoppingCartApi;
import ru.yandex.practicum.api.ShoppingStoreApi;
import ru.yandex.practicum.api.WarehouseApi;
import ru.yandex.practicum.enums.order.OrderState;

import ru.yandex.practicum.exception.order.BadOrderStateException;
import ru.yandex.practicum.exception.order.IncorrectNumberOfReturnedItemsException;
import ru.yandex.practicum.exception.order.OrderNotFoundException;
import ru.yandex.practicum.exception.shoppingStore.ProductNotFoundException;
import ru.yandex.practicum.interfaces.OrderRepository;
import ru.yandex.practicum.interfaces.OrderService;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;

import java.util.*;

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

        // Внедрить проверку статуса заказа и корзины на стороне их сервисов (добавить методы возвращающие статус)

        Order newOrder = mapper.toOrder(dto);
        // Статус заказа устанавливается дефолтно на NEW

        newOrder.setFragile(productsDto.getFragile());
        newOrder.setDeliveryWeight(productsDto.getDeliveryWeight());
        newOrder.setDeliveryVolume(productsDto.getDeliveryVolume());
        newOrder.setState(OrderState.ON_PAYMENT);

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
    public OrderDto payForTheOrder(UUID orderId) {

        Order order = repository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException(""));

        if (!order.getState().equals(OrderState.ON_PAYMENT)) {
            log.info("Статус заказа не соответствует ожидаемому. State: {}", order.getState());
            throw new BadOrderStateException("");
        }

        List<ProductDto> products = shoppingStoreApi.getProductsByIds(order.getProducts().keySet());
        Map<UUID, Integer> quantityMap = order.getProducts();

        List<OrderPaymentRequest> paymentRequests = products.stream()
                .map(product -> OrderPaymentRequest.builder()
                        .productId(product.getProductId())
                        .price(product.getPrice())
                        .quantity(quantityMap.get(product.getProductId()))
                        .build()).toList();

        // сервис оплаты.enrichOrderWithPayment(order.getOrderId, paymentRequests);

        // Dto сервиса оплаты со всеми вытекающими исключениями и проверками

         /*
        order.setPaymentId();
        order.setTotalPrice();
        order.setProductPrice();
        */

        return mapper.toDto(order);
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

        Map<UUID, Integer> failure = new HashMap<>();
        HashMap<UUID, Integer> newOrder = new HashMap<>(order.getProducts());

        for (UUID key : productsReturn.keySet()) {
            if (newOrder.get(key) < productsReturn.get(key)) {
                failure.put(key, Math.toIntExact(productsReturn.get(key) - newOrder.get(key)));
            } else if (failure.isEmpty()) {
                newOrder.replace(key, Math.toIntExact(order.getProducts().get(key) - productsReturn.get(key)));
            }
        }

        if (!failure.isEmpty()) {
            throw new IncorrectNumberOfReturnedItemsException("Зафиксирована попытка вернуть большее количество" +
                    " товаров. Превышение у ID: " + failure.keySet() + "в количестве: " + failure.values() + " соответственно.");
        }

        order.setProducts(newOrder);
        order.setState(OrderState.PRODUCT_RETURNED);


        warehouseApi.increaseProductQuantity(AddProductToWarehouseRequest.builder().products(productsReturn).build(),
                OrderState.PRODUCT_RETURNED);

        // Перерасчет стоимости заказа и доставки !
        // Перерасчет условий доставки

        return mapper.toDto(order);
    }

    /*
     * Метод для вызова эндпоинтов:
     * /api/v1/order/payment
     * /api/v1/order/payment/failed
     * /api/v1/order/payment/failed
     * и т.п.
     */
    @Override
    @Transactional
    public OrderDto setOrderState(UUID orderId, OrderState state) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Не найден заказ с id: " + orderId));

        switch (state) {
            case OrderState.NEW -> order.setState(OrderState.NEW);
            case OrderState.PRODUCT_RETURNED -> order.setState(OrderState.PRODUCT_RETURNED);
            case OrderState.PAID -> order.setState(OrderState.PAID);
            case OrderState.PAYMENT_FAILED -> order.setState(OrderState.PAYMENT_FAILED);
            case OrderState.DELIVERED -> order.setState(OrderState.DELIVERED);
            case OrderState.ASSEMBLED -> order.setState(OrderState.ASSEMBLED);
            case OrderState.DELIVERY_FAILED -> order.setState(OrderState.DELIVERY_FAILED);
            case OrderState.ASSEMBLY_FAILED -> order.setState(OrderState.ASSEMBLY_FAILED);
            case OrderState.CANCELED -> order.setState(OrderState.CANCELED);
            case OrderState.COMPLETED -> order.setState(OrderState.COMPLETED);
            case OrderState.DONE -> order.setState(OrderState.DONE);
            case OrderState.ON_DELIVERY -> order.setState(OrderState.ON_DELIVERY);
            case OrderState.ON_PAYMENT -> order.setState(OrderState.ON_PAYMENT);
        }

        return mapper.toDto(order);
    }
}
