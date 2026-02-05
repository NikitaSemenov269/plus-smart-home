package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.DTO.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interfaces.WarehouseRepository;
import ru.yandex.practicum.interfaces.WarehouseService;
import ru.yandex.practicum.mapper.WarehouseMapper;

import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];


    @Override
    @Transactional
    public void addNewProductToTheWarehouse(NewProductInWarehouseRequest newProduct) {

    }
}
