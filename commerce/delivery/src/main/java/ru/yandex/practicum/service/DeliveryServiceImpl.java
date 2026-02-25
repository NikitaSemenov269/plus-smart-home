package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;
import ru.yandex.practicum.enums.delivery.DeliveryState;
import ru.yandex.practicum.interfaces.DeliveryInterface;
import ru.yandex.practicum.interfaces.DeliveryRepository;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.BookedProductMapper;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryInterface {
    private final DeliveryRepository repository;
    private final DeliveryMapper deliveryMapper;
    private final AddressMapper addressMapper;
    private final BookedProductMapper bookedProductMapper;

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(5.0);
    private static final BigDecimal FRAGILE_GOODS = BigDecimal.valueOf(0.2);
    private static final BigDecimal STREET_MISMATCH = BigDecimal.valueOf(0.2);
    private static final BigDecimal WEIGHT_COEFFICIENT = BigDecimal.valueOf(0.3);

    @Override
    @Transactional
    public DeliveryResponse createDeliveryRequest(DeliveryRequest dto) {
        Optional<Delivery> existingDelivery = repository.findByOrderId(dto.getOrderId());

        if (existingDelivery.isPresent()) {
            log.info("Заявка на доставку заказа с ID {} уже существует.", dto.getOrderId());
            return deliveryMapper.toDtoResponse(existingDelivery.get());
        }

        Delivery delivery = deliveryMapper.toEntity(dto);

        repository.save(delivery);
        return deliveryMapper.toDtoResponse(delivery);
    }

    @Override
    @Transactional
    public DeliveryResponse payToDelivery(UUID deliveryId) {
        Delivery delivery = repository.findById(deliveryId).orElseThrow(
                () -> new NotFoundException("FFFFF"));

        if (!delivery.getDeliveryState().equals(DeliveryState.CREATED)) {
            log.info("");
            return deliveryMapper.toDtoResponse(delivery);
        }

        BigDecimal shippingCost =

                delivery.setShippingCost(shippingCost);

        return deliveryMapper.toDtoResponse(delivery);
    }


}
