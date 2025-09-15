package com.example.store.service;

import com.example.store.TestContainerInitialization;
import com.example.store.dto.AllStoresResponseDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;


//TODO: − Нет кейсов:
// email пустой;
// удаление несуществующей записи (ожидается EmptyResultDataAccessException от deleteById);
// проверка, что updatedAt реально меняется при update; проверка всех полей в DTO (проверяется только name).

@SpringBootTest
@Transactional
class StoreServiceTest extends TestContainerInitialization {

    public static final String DEFAULT_STORE_LOCATION = "ул. Ленина";
    public static final String DEFAULT_STORE_NAME = "Пятёрочка";
    public static final String DEFAULT_STORE_EMAIL = "mail@ya.ru";

    @Autowired
    private StoreRepository repository;

    @Autowired
    private StoreService service;


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

    @Test
    void findAllStores_whenStoresNotExist_thenEmptyList() {
        Assertions.assertEquals(0, service.findAllStores().size());
    }

    @Test
    void findAllStores_whenThereAreSeveralStores_thenReturn() {

        Store firstStore = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        Store secondStore = createStore("Красный яр", "ул. Мира", "krasyar@mail.ru");
        Store thirdStore = createStore("Магнит", "ул. Мате Залки", "magnit@mail.ru");

        List<AllStoresResponseDto> allStores = service.findAllStores();

        Assertions.assertEquals(3, allStores.size());
        Assertions.assertEquals(firstStore.getName(), allStores.get(0).getName());
        Assertions.assertEquals(secondStore.getName(), allStores.get(1).getName());
        Assertions.assertEquals(thirdStore.getName(), allStores.get(2).getName());

    }

    @Test
    void findByLocation_whenStoresNotExist_thenEmptyList() {
        Assertions.assertEquals(0, service.findByLocation("ул. Урванцева").size());
    }

    @Test
    void findByLocation_whenThereAreNoStoreWithSuchLocation_thenEmptyList() {

        createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        createStore("Красный яр", "ул. Мира", "krasyar@mail.ru");
        createStore("Магнит", "ул. Мате Залки", "magnit@mail.ru");

        Assertions.assertEquals(0, service.findByLocation("ул. Урванцева").size());

    }

    @Test
    void findByLocation_whenThereAreStoreWithSuchLocation_thenReturn() {

        createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        Store secondStore = createStore("Красный яр", "ул. Мира", "krasyar@mail.ru");
        Store thirdStore = createStore("Магнит", "ул. Мира", "magnit@mail.ru");

        List<AllStoresResponseDto> stores = service.findByLocation("ул. Мира");

        Assertions.assertEquals(2, stores.size());
        Assertions.assertEquals(secondStore.getName(), stores.get(0).getName());
        Assertions.assertEquals(thirdStore.getName(), stores.get(1).getName());

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
