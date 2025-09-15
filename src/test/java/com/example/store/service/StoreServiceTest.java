package com.example.store.service;

import com.example.store.TestContainerInitialization;
import com.example.store.dto.AllStoresResponseDto;
import com.example.store.dto.ProductResponseDto;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Product;
import com.example.store.entity.Store;
import com.example.store.entity.StoreProduct;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.StoreProductRepository;
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

import java.math.BigDecimal;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StoreProductRepository storeProductRepository;


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

        int size = service.findByLocation("ул. Урванцева").size();

        Assertions.assertEquals(0, size);

    }

    @Test
    void findByLocation_whenThereAreNoStoreWithSuchLocation_thenEmptyList() {

        createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        createStore("Красный яр", "ул. Мира", "krasyar@mail.ru");
        createStore("Магнит", "ул. Мате Залки", "magnit@mail.ru");

        int size = service.findByLocation("ул. Урванцева").size();

        Assertions.assertEquals(0, size);

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

    @Test
    void findAllProductByLocation_whenStoresNotExist_thenEmptyList() {

        createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        createProduct("Кока-кола", BigDecimal.valueOf(73.67), "Напитки");
        createProduct("Квас", BigDecimal.valueOf(55.12), "Напитки");

        List<ProductResponseDto> someStreet = Assertions.assertDoesNotThrow(() -> service.findAllProductByLocation("Some Street"));

        Assertions.assertTrue(someStreet.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoresNotFoundByLocation_thenEmptyList() {

        createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        createProduct("Кока-кола", BigDecimal.valueOf(73.67), "Напитки");
        createProduct("Квас", BigDecimal.valueOf(55.12), "Напитки");

        createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        createStore("Красный Яр", "ул. 9 Мая", "five@mail.ru");

        List<ProductResponseDto> someStreet = service.findAllProductByLocation("Some Street");

        Assertions.assertTrue(someStreet.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenLocationIsNull_thenThrow() {

        Store firstStore = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
/*
        Store secondStore = createStore("Красный Яр", "ул. 9 Мая", "five@mail.ru");
        Store thirdStore = createStore("Красный Яр", "ул. 9 Мая", "five@mail.ru");

*/

        Product firstProduct = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product secondProduct = createProduct("Кока-кола", BigDecimal.valueOf(73.67), "Напитки");
        Product thirdProduct = createProduct("Квас", BigDecimal.valueOf(55.12), "Напитки");

        createProductStore(firstStore.getId(), firstProduct.getId());
        createProductStore(firstStore.getId(), secondProduct.getId());
        createProductStore(firstStore.getId(), thirdProduct.getId());


        Assertions.assertThrows(NullPointerException.class, () -> service.findAllProductByLocation(null));

    }

    @Test
    void findAllProductByLocation_whenStoreProductNotExist_thenEmptyList() {

        createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
/*
        Store secondStore = createStore("Красный Яр", "ул. 9 Мая", "five@mail.ru");
        Store thirdStore = createStore("Красный Яр", "ул. 9 Мая", "five@mail.ru");

*/

        createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        createProduct("Кока-кола", BigDecimal.valueOf(73.67), "Напитки");
        createProduct("Квас", BigDecimal.valueOf(55.12), "Напитки");

        List<ProductResponseDto> someStreet =
                Assertions.assertDoesNotThrow(() -> service.findAllProductByLocation(DEFAULT_STORE_LOCATION));

        Assertions.assertTrue(someStreet.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenProductNotExist_thenEmptyList() {

        Store firstStore = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        createStore(DEFAULT_STORE_NAME, "SomeStreet", DEFAULT_STORE_EMAIL);

        Product firstProduct = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product secondProduct = createProduct("Кока-кола", BigDecimal.valueOf(73.67), "Напитки");
        Product thirdProduct = createProduct("Квас", BigDecimal.valueOf(55.12), "Напитки");

        createProductStore(firstStore.getId(), firstProduct.getId());
        createProductStore(firstStore.getId(), secondProduct.getId());
        createProductStore(firstStore.getId(), thirdProduct.getId());

        List<ProductResponseDto> someStreet =
                Assertions.assertDoesNotThrow(() -> service.findAllProductByLocation("SomeStreet"));

        Assertions.assertTrue(someStreet.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreAndProductExist_thenReturn() {

        Store firstStore = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        Store secondStore = createStore(DEFAULT_STORE_NAME, "SomeStreet", DEFAULT_STORE_EMAIL);

        Product firstProduct = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product secondProduct = createProduct("Кока-кола", BigDecimal.valueOf(73.67), "Напитки");
        Product thirdProduct = createProduct("Квас", BigDecimal.valueOf(55.12), "Напитки");
        Product fourthProduct = createProduct("Джин", BigDecimal.valueOf(55.12), "Напитки");

        createProductStore(firstStore.getId(), firstProduct.getId());
        createProductStore(firstStore.getId(), secondProduct.getId());
        createProductStore(firstStore.getId(), thirdProduct.getId());
        createProductStore(secondProduct.getId(), secondProduct.getId());
        createProductStore(secondStore.getId(), thirdProduct.getId());
        createProductStore(secondStore.getId(), fourthProduct.getId());

        List<ProductResponseDto> allProductByLocation =
                Assertions.assertDoesNotThrow(() -> service.findAllProductByLocation(DEFAULT_STORE_LOCATION));

        Assertions.assertEquals(3, allProductByLocation.size());
        Assertions.assertEquals(firstProduct.getId(), allProductByLocation.get(0).getId());

    }

    @Test
    void findUniqueProducts_whenUniqueProductsExist_thenReturnUniqueProductList() {

        Store firstStore = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        Store secondStore = createStore("Красный Яр", "SomeStreet", DEFAULT_STORE_EMAIL);

        Product firstProduct = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product secondProduct = createProduct("Кока-кола", BigDecimal.valueOf(73.67), "Напитки");
        Product thirdProduct = createProduct("Квас", BigDecimal.valueOf(55.12), "Напитки");

        createProductStore(firstStore.getId(), firstProduct.getId());
        createProductStore(firstStore.getId(), secondProduct.getId());
        createProductStore(firstStore.getId(), thirdProduct.getId());
        createProductStore(secondProduct.getId(), secondProduct.getId());
        createProductStore(secondStore.getId(), thirdProduct.getId());

        List<ProductResponseDto> result = Assertions.assertDoesNotThrow(() -> service.findUniqueProducts());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(firstProduct.getName(), result.get(0).getName());

    }

    private StoreProduct createProductStore(UUID storeId, UUID productId) {

        StoreProduct storeProduct = new StoreProduct(UUID.randomUUID(), storeId, productId);

        storeProduct = storeProductRepository.saveAndFlush(storeProduct);

        return storeProduct;

    }

    private Product createProduct(String name, BigDecimal price, String category) {

        Product product = new Product(UUID.randomUUID(), name, price, category);

        product = productRepository.saveAndFlush(product);

        return product;

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
