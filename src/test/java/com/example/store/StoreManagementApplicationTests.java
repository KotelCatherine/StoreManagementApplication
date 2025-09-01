package com.example.store;

import com.example.store.dto.StoreResponseDto;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import com.example.store.service.StoreService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

@SpringBootTest
@Transactional
class StoreManagementApplicationTests extends TestContainerInitialization {

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

        StoreRequest storeRequest = createStoreRequest("", "ул. Урванцева");

        Assertions.assertThrows(MethodArgumentNotValidException.class, ()-> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenLocationIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("Красный Яр", "");

        Assertions.assertThrows(MethodArgumentNotValidException.class, ()-> service.createStore(storeRequest));

    }

    @Test
    void create_whenNameAndLocationNotBlank_thenCreate() {

        StoreRequest storeRequest = createStoreRequest("Пятёрочка", "ул. Урванцева");

        StoreResponseDto storeResponseDto = Assertions.assertDoesNotThrow(() -> service.createStore(storeRequest));

        Assertions.assertEquals(storeRequest.getName(), storeResponseDto.getName());

    }

    private StoreRequest createStoreRequest(String name, String location) {
        return new StoreRequest(name, location);
    }

}
