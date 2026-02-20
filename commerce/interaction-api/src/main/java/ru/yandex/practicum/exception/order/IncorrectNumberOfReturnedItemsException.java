package ru.yandex.practicum.exception.order;

public class IncorrectNumberOfReturnedItemsException extends RuntimeException {
    public IncorrectNumberOfReturnedItemsException(String message) {
        super(message);
    }
}
