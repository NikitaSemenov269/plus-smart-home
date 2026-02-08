package ru.yandex.practicum.mapper;

import org.mapstruct.*;
import ru.yandex.practicum.DTO.shoppingStore.ProductDto;
import ru.yandex.practicum.model.ProductOfStore;

@Mapper(componentModel = "spring")
public interface ShoppingStoreMapper {

    ProductOfStore toProduct(ProductDto dto);

    ProductDto toDto(ProductOfStore product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productId", ignore = true)
    void updateProduct(ProductDto dto, @MappingTarget ProductOfStore product);
}
