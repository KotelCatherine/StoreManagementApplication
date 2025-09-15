package com.example.store.service;

import com.example.store.TestContainerInitialization;
import com.example.store.dto.SupplierResponseDto;
import com.example.store.entity.Supplier;
import com.example.store.repository.SupplierRepository;
import com.example.store.request.SupplierRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
class SupplierServiceTest extends TestContainerInitialization {

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private SupplierService service;

    @ParameterizedTest
    @MethodSource("requestInvalid")
    void createSupplier_whenRequestInvalid_thenThrow(String name, String email, String website) {

        SupplierRequest supplierRequest = createSupplierRequest(name, email, "51688", "г.Красноярск, ул. Глинки", website);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createSupplier(supplierRequest));

    }

    @Test
    void createSupplier_whenRequestValid_thenCreated() {

        SupplierRequest supplierRequest = createSupplierRequest("Поставщик", "supplier@mail.ru", "51688", "г.Красноярск, ул. Глинки", "https://supplier.ru");
        SupplierResponseDto supplierDto = service.createSupplier(supplierRequest);

        Assertions.assertEquals(supplierRequest.getName(), supplierDto.getName());

    }

    @Test
    void findSupplierById_whenIdInvalid_thenThrow() {

        createSupplier("Поставщик", "supplier@mail.ru", "51688", "г.Красноярск, ул. Глинки", "https://supplier.ru");

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.findSupplierById(UUID.fromString("123")));

    }

    @Test
    void findSupplierById_whenIdValidButSupplierNotExist_thenThrow() {

        createSupplier("Поставщик", "supplier@mail.ru", "51688", "г.Красноярск, ул. Глинки", "https://supplier.ru");

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findSupplierById(UUID.randomUUID()));

    }

    @Test
    void findSupplierById_whenIdValidAndSupplierExist_thenReturn() {

        Supplier supplier = createSupplier("Поставщик", "supplier@mail.ru", "51688", "г.Красноярск, ул. Глинки", "https://supplier.ru");

        SupplierResponseDto supplierById = service.findSupplierById(supplier.getId());

        Assertions.assertEquals(supplier.getName(), supplierById.getName());

    }

    @Test
    void updateSupplier_whenIdInvalid_thenThrow() {

        createSupplier("Поставщик", "supplier@mail.ru", "51688", "г.Красноярск, ул. Глинки", "https://supplier.ru");

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.findSupplierById(UUID.fromString("123")));

    }

    @ParameterizedTest
    @MethodSource("requestInvalid")
    void updateSupplier_whenRequestInvalid_thenThrow(String name, String email, String website) {

        Supplier supplier = createSupplier("Supplier", "email@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");
        SupplierRequest supplierRequest = createSupplierRequest(name, email, "51688", "г.Красноярск, ул. Глинки", website);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.updateSupplier(supplier.getId(), supplierRequest));

    }

    @Test
    void updateSupplier_whenSupplierByIdNotFound_theThrow() {

        createSupplier("Supplier", "email@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");
        SupplierRequest supplierRequest = createSupplierRequest("SupplierNumberOne", "malya@mail.ry", "51655", "г.Красноярск, ул. Авиаторов", "https://webSupplier.com");

        Assertions.assertThrows(NoSuchElementException.class, ()->service.updateSupplier(UUID.randomUUID(), supplierRequest));

    }

    @Test
    void updateSupplier_whenRequestAndIdValidated_thenUpdate() {

        Supplier supplier = createSupplier("Supplier", "email@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");
        SupplierRequest supplierRequest = createSupplierRequest("SupplierNumberOne", "malya@mail.ry", "51655", "г.Красноярск, ул. Авиаторов", "https://webSupplier.com");

        SupplierResponseDto supplierDto = service.updateSupplier(supplier.getId(), supplierRequest);

        Assertions.assertEquals(supplier.getId(), supplierDto.getId());

    }

    @Test
    void deleteStore_whenIdInvalid_thenThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteSupplier(UUID.fromString("123")));
    }

    @Test
    void deleteStore_whenIdValid_thenDelete() {

        Supplier supplier = createSupplier("Supplier", "email@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");

        Assertions.assertDoesNotThrow(() -> service.deleteSupplier(supplier.getId()));

    }

    @Test
    void getAllSuppliers_whenRepositoryEmpty_thenEmptyPage() {

        Pageable pageable = PageRequest.of(0, 5);

        Assertions.assertTrue(service.getAllSuppliers(pageable).isEmpty());

    }

    @Test
    void getAllSuppliers_whenSuppliersExist(){

        Supplier firstSupplier = createSupplier("Supplier1", "email1@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");
        Supplier secondSupplier = createSupplier("Supplier2", "email2@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");
        Supplier thirdSupplier = createSupplier("Supplier3", "email3@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");
        Supplier fourthSupplier = createSupplier("Supplier4", "email4@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");
        Supplier fivthSupplier = createSupplier("Supplier5", "email5@mail.ry", "51688", "г.Красноярск, ул. Глинки", "https://website.com");

        Pageable pageable = PageRequest.of(0, 3);

        Page<SupplierResponseDto> suppliers = service.getAllSuppliers(pageable);



        suppliers.forEach(System.out::println);

    }

    @Test
    void updateContactSupplier() {
    }

    private Supplier createSupplier(String name, String email, String phone, String address, String website) {

        Supplier supplier = new Supplier(UUID.randomUUID(), name, email, phone, address, website, null);

        repository.saveAndFlush(supplier);

        return supplier;


    }

    private SupplierRequest createSupplierRequest(String name, String email, String number, String address, String website) {
        return new SupplierRequest(name, email, number, address, website);
    }

    private Stream<Arguments> requestInvalid() {
        return Stream.of(
                Arguments.of("", "supplier@mail.ru", "https://some.ru"),
                Arguments.of("Поставщик раз", "", "https://some.ru"),
                Arguments.of("Поставщик два", "supplier@mail.ru", "https//some.ru"),
                Arguments.of("", "", "https:/some.ru")
        );
    }

}
