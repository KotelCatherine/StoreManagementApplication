package com.example.store.mapper;

import com.example.store.dto.SupplierResponseDto;
import com.example.store.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierResponseDto mapToSupplierDto(Supplier supplier) {
        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getWebsite(),
                supplier.getUpdatedAt());
    }


}
