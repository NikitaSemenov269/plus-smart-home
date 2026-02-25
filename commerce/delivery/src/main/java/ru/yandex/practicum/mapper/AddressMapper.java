package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.DTO.warehouse.AddressDto;
import ru.yandex.practicum.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressDto dto);

    AddressDto toDto(Address address);
}
