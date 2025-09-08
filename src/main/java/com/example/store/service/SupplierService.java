package com.example.store.service;

import com.example.store.dto.SupplierResponseDto;
import com.example.store.entity.Supplier;
import com.example.store.mapper.SupplierMapper;
import com.example.store.repository.SupplierRepository;
import com.example.store.request.SupplierContactRequest;
import com.example.store.request.SupplierRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SupplierService {

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private SupplierMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponseDto createSupplier(@Valid SupplierRequest request) {

        Supplier supplier = new Supplier(
                UUID.randomUUID(),
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress(),
                request.getWebsite(),
                null);

        repository.saveAndFlush(supplier);

        return mapper.mapToSupplierDto(supplier);

    }

    public SupplierResponseDto findSupplierById(UUID id) {

        Supplier supplier = repository.findById(id)
                .orElseThrow();

        return mapper.mapToSupplierDto(supplier);
    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponseDto updateSupplier(UUID id, @Valid SupplierRequest request) {

        Supplier supplier = repository.findById(id)
                .orElseThrow();

        supplier.setName(request.getName());
        supplier.setAddress(request.getAddress());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setWebsite(request.getWebsite());

        repository.saveAndFlush(supplier);

        return mapper.mapToSupplierDto(supplier);

    }

    public void deleteSupplier(UUID id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SupplierResponseDto> getAllSuppliers(Pageable pageable) {

        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }

        Page<Supplier> suppliers = repository.findAll(pageable);

        return suppliers.map(mapper::mapToSupplierDto);

    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponseDto updateContactSupplier(UUID id, @Valid SupplierContactRequest request) {

        Supplier supplier = repository.findById(id)
                .orElseThrow();

        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setWebsite(request.getWebsite());

        repository.saveAndFlush(supplier);

        return mapper.mapToSupplierDto(supplier);

    }
}
