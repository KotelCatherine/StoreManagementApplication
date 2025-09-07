package com.example.store.service;

import com.example.store.TestContainerInitialization;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
class StoreServiceTest extends TestContainerInitialization {

    public static final String DEFAULT_STORE_LOCATION = "ул. Ленина";
    @Autowired
    private StoreRepository repository;

    @Autowired
    private StoreService service;

    public static final String DEFAULT_STORE_NAME = "Пятёрочка";
    public static final String DEFAULT_STORE_EMAIL = "mail@ya.ru";


    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @Test
    void createStore_whenNameIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("", DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenLocationIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest(DEFAULT_STORE_NAME, "", DEFAULT_STORE_EMAIL);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void create_whenNameAndLocationNotBlank_thenCreate() {

        StoreRequest storeRequest = createStoreRequest(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        StoreResponseDto storeResponseDto = Assertions.assertDoesNotThrow(() -> service.createStore(storeRequest));

        Assertions.assertEquals(storeRequest.getName(), storeResponseDto.getName());

    }

    @Test
    void deleteStore_whenIdInvalid_thenThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteStore(UUID.fromString("123")));
    }

    @Test
    void deleteStore_whenIdValid_thenDelete() {

        Store store = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        Assertions.assertDoesNotThrow(() -> service.deleteStore(store.getId()));

    }

    @Test
    void findStoreById_whenIdInvalid_thenThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.findStoreById(UUID.fromString("123")));
    }

    @Test
    void findStoreById_whenIdValid_thenFind() {

        Store store = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        StoreResponseDto storeResponseDto = Assertions.assertDoesNotThrow(() -> service.findStoreById(store.getId()));

        Assertions.assertEquals(store.getName(), storeResponseDto.getName());

    }

    @Test
    void findStoreById_whenStoreByIdNotFound_thenThrows() {
        Assertions.assertThrows(NoSuchElementException.class, () -> service.findStoreById(UUID.randomUUID()));
    }


    @ParameterizedTest
    @MethodSource("invalidData")
    void updateStore_whenRequestInvalid_thenThrow(String name, String location) {

        Store store = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        StoreRequest storeRequest = createStoreRequest(name, location, DEFAULT_STORE_EMAIL);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.updateStore(store.getId(), storeRequest));

    }

    @Test
    void updateStore_whenStoreNotFoundById_thenThrow() {

        createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        StoreRequest storeRequest = createStoreRequest(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateStore(UUID.randomUUID(), storeRequest));

    }

    @Test
    void updateStore_whenStoreExist_thenUpdate() {

        Store store = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        StoreRequest storeRequest = createStoreRequest("Красный яр", "ул. Димитрова", DEFAULT_STORE_EMAIL);

        StoreResponseDto storeResponseDto = Assertions.assertDoesNotThrow(() -> service.updateStore(store.getId(), storeRequest));
        Assertions.assertEquals(storeRequest.getName(), storeResponseDto.getName());

    }

    private Store createStore(String name, String location, String email) {

        Store store = new Store(UUID.randomUUID(), name, location, null, email);
        store = repository.saveAndFlush(store);

        return store;

    }

    private StoreRequest createStoreRequest(String name, String location, String email) {
        return new StoreRequest(name, location, email);
    }

    private Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of("", "ул.Урванцева"),
                Arguments.of("Красный Яр", ""),
                Arguments.of("", "")
        );
    }

}
