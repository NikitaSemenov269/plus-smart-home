package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;
import ru.yandex.practicum.api.OrderApi;
import ru.yandex.practicum.enums.delivery.DeliveryState;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.interfaces.DeliveryInterface;
import ru.yandex.practicum.interfaces.DeliveryRepository;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryInterface {
    private final DeliveryRepository repository;
    private final DeliveryMapper mapper;
    private final OrderApi orderApi;

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(5.0);
    private static final BigDecimal FRAGILE_GOODS = BigDecimal.valueOf(0.2);
    private static final BigDecimal STREET_MISMATCH = BigDecimal.valueOf(0.2);
    private static final BigDecimal VOLUME_COEFFICIENT = BigDecimal.valueOf(0.2);
    private static final BigDecimal WEIGHT_COEFFICIENT = BigDecimal.valueOf(0.3);

    @Override
    @Transactional
    public DeliveryResponse createDeliveryRequest(DeliveryRequest dto) {
        Optional<Delivery> existingDelivery = repository.findByOrderId(dto.getOrderId());

        if (existingDelivery.isPresent()) {
            log.info("Заявка на доставку заказа с ID {} уже существует.", dto.getOrderId());
            return mapper.toDtoResponse(existingDelivery.get());
        }

        Delivery delivery = mapper.toEntity(dto);

        repository.save(delivery);
        return mapper.toDtoResponse(delivery);
    }

    @Override
    @Transactional
    public void payToDelivery(UUID deliveryId) {
        Delivery delivery = searchDelivery(deliveryId);

        if (!delivery.getDeliveryState().equals(DeliveryState.CREATED)) {
            log.info("Доставка {} уже обработана, текущий статус: {}", deliveryId, delivery.getDeliveryState());
        }

        delivery.setShippingCost(calculateDelivery(delivery));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
    }

    @Override
    public BigDecimal calculateDelivery(UUID deliveryId) {
        return calculateDelivery(searchDelivery(deliveryId));
    }

    @Override
    public BigDecimal calculateDelivery(Delivery delivery) {
        BigDecimal shippingCost = BASE_RATE;

        String warehouseStreet = delivery.getAddressOfWarehouse().getStreet();
        BigDecimal warehouseMultiplier;
        if (warehouseStreet.contains("ADDRESS_2")) {
            warehouseMultiplier = BigDecimal.valueOf(2);
        } else if (warehouseStreet.contains("ADDRESS_1")) {
            warehouseMultiplier = BigDecimal.ONE;
        } else {
            warehouseMultiplier = BigDecimal.ONE;
        }
        shippingCost = shippingCost.add(shippingCost.multiply(warehouseMultiplier));

        Boolean fragile = delivery.getBookedProducts().getFragile();
        if (fragile != null && fragile) {
            shippingCost = shippingCost.add(shippingCost.multiply(FRAGILE_GOODS));
        }

        Double weight = delivery.getBookedProducts().getDeliveryWeight();
        if (weight != null) {
            shippingCost = shippingCost.add(
                    BigDecimal.valueOf(weight).multiply(WEIGHT_COEFFICIENT)
            );
        }

        Double volume = delivery.getBookedProducts().getDeliveryVolume();
        if (volume != null) {
            shippingCost = shippingCost.add(
                    BigDecimal.valueOf(volume).multiply(VOLUME_COEFFICIENT)
            );
        }

        if (!delivery.getAddressOfClient().getStreet()
                .equals(delivery.getAddressOfWarehouse().getStreet())) {
            shippingCost = shippingCost.add(shippingCost.multiply(STREET_MISMATCH));
        }

        return shippingCost.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    // Мне не сильно нравится такой подход, но ничего лучше не придумал.
    public DeliveryResponse setDeliveryState(UUID deliveryId, DeliveryState state) {
        Delivery delivery = searchDelivery(deliveryId);
        switch (state) {
            case DeliveryState.CREATED -> {
                delivery.setDeliveryState(DeliveryState.CREATED);
                orderApi.setOrderState(delivery.getOrderId(), OrderState.ON_DELIVERY);
            }
            case DeliveryState.IN_PROGRESS -> {
                delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
                orderApi.setOrderState(delivery.getOrderId(), OrderState.ASSEMBLED);
            }
            case DeliveryState.DELIVERED -> {
                delivery.setDeliveryState(DeliveryState.DELIVERED);
                orderApi.setOrderState(delivery.getOrderId(), OrderState.COMPLETED);
            }
            case DeliveryState.FAILED -> {
                delivery.setDeliveryState(DeliveryState.FAILED);
                orderApi.setOrderState(delivery.getOrderId(), OrderState.DELIVERY_FAILED);
            }
            case DeliveryState.CANCELLED -> {
                delivery.setDeliveryState(DeliveryState.CANCELLED);
                orderApi.setOrderState(delivery.getOrderId(), OrderState.DELIVERY_FAILED);
            }
        }

        repository.save(delivery);
        return mapper.toDtoResponse(delivery);
    }

    @Transactional(readOnly = true)
    private Delivery searchDelivery(UUID deliveryId) {
        return repository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Доставка не найдена: " + deliveryId));
    }
}
