package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.DTO.warehouse.BookedProductsDto;
import ru.yandex.practicum.model.BookedProducts;

@Mapper(componentModel = "spring")
public interface BookedProductMapper {

    BookedProducts toBookedProducts(BookedProductsDto dto);

}
