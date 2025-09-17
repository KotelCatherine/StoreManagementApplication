package com.example.store.controller;

import com.example.store.dto.AllStoresResponseDto;
import com.example.store.dto.ProductResponseDto;
import com.example.store.dto.StoreResponseDto;
import com.example.store.request.ProductRequest;
import com.example.store.request.StoreRequest;
import com.example.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stores")
@Validated
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @RequestBody StoreRequest request) {

        StoreResponseDto storeResponseDto = storeService.createStore(request);

        return ResponseEntity.ok(storeResponseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> findStoreById(@PathVariable UUID id) {

        StoreResponseDto store = storeService.findStoreById(id);

        return ResponseEntity.ok(store);

    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable UUID id, @Valid @RequestBody StoreRequest request
    ) {

        StoreResponseDto updatedStore = storeService.updateStore(id, request);

        return ResponseEntity.ok(updatedStore);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID id) {

        storeService.deleteStore(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/all")
    public ResponseEntity<List<AllStoresResponseDto>> findAllStores() {

        List<AllStoresResponseDto> allStoresResponses = storeService.findAllStores();

        return ResponseEntity.ok(allStoresResponses);

    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<AllStoresResponseDto>> findStoreByLocation(@PathVariable String location) {

        List<AllStoresResponseDto> allStoresResponses = storeService.findByLocation(location);

        return ResponseEntity.ok(allStoresResponses);

    }

    @GetMapping("/sorted")
    public ResponseEntity<List<AllStoresResponseDto>> findAllStoresByName() {

        List<AllStoresResponseDto> allStoresResponses = storeService.findAllStoresByName();

        return ResponseEntity.ok(allStoresResponses);

    }

    @GetMapping("/{id}/copy")
    public ResponseEntity<StoreResponseDto> copyStore(@PathVariable UUID id) {

        StoreResponseDto copyStoreResponseDto = storeService.copy(id);

        return ResponseEntity.ok(copyStoreResponseDto);

    }

    @GetMapping("/product/by_location")
    @Operation(summary = "Найти товары во всех магазинах на указанной улице",
            description = "Все товары в магазинах на указанной улице")
    public ResponseEntity<List<ProductResponseDto>> findAllProductByLocation(
            @Parameter(description = "Название улицы")
            @RequestParam String location
    ) {

        List<ProductResponseDto> allProducts = storeService.findAllProductByLocation(location);

        return ResponseEntity.ok(allProducts);

    }

    @GetMapping("/products/unique")
    @Operation(summary = "Найти уникальные товары", description = "Товары, которые продаются только в одном магазине")
    public ResponseEntity<List<ProductResponseDto>> findUniqueProducts() {
        return ResponseEntity.ok(storeService.findUniqueProducts());
    }

    @PostMapping("/product/{storeId}")
    public ResponseEntity<ProductResponseDto> createProduct(@PathVariable UUID storeId, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(storeService.createProduct(storeId, request));
    }


}
