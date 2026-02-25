package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.DTO.delivery.DeliveryRequest;
import ru.yandex.practicum.DTO.delivery.DeliveryResponse;
import ru.yandex.practicum.model.Delivery;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, BookedProductMapper.class})
public interface DeliveryMapper {

    DeliveryResponse toDtoResponse(Delivery delivery);

    Delivery toEntity(DeliveryRequest request);

}
