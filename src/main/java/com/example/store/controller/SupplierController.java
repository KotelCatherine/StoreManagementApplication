package com.example.store.controller;

import com.example.store.dto.SupplierResponseDto;
import com.example.store.request.SupplierContactRequest;
import com.example.store.request.SupplierRequest;
import com.example.store.service.SupplierService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/suppliers")
@Validated
public class SupplierController {

    @Autowired
    private SupplierService service;

    @PostMapping
    public ResponseEntity<SupplierResponseDto> createSupplier(@Valid @RequestBody SupplierRequest request) {

        SupplierResponseDto supplier = service.createSupplier(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(supplier);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> findSupplierById(@PathVariable UUID id) {

        SupplierResponseDto supplier = service.findSupplierById(id);

        return ResponseEntity.ok(supplier);

    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplier(@PathVariable UUID id, @Valid @RequestBody SupplierRequest request
    ) {

        SupplierResponseDto supplier= service.updateSupplier(id, request);

        return ResponseEntity.ok(supplier);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {

        service.deleteSupplier(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<Page<SupplierResponseDto>> getPageWithSuppliers(@ParameterObject Pageable pageable) {

        service.getAllSuppliers(pageable);

        return ResponseEntity.ok(service.getAllSuppliers(pageable));

    }

    @PatchMapping("/{id}/contact")
    public ResponseEntity<SupplierResponseDto> updateContact(@PathVariable UUID id, @RequestBody SupplierContactRequest request) {

        SupplierResponseDto supplier = service.updateContactSupplier(id, request);

        return ResponseEntity.ok(supplier);

    }

}
