package com.example.store.mapper;

import com.example.store.dto.AllStoresResponseDto;
import com.example.store.dto.ProductResponseDto;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Product;
import com.example.store.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public StoreResponseDto mapToStoreResponseDto(Store store) {
        return new StoreResponseDto(store.getId(), store.getName(), store.getLocation(), store.getEmail());
    }

    public AllStoresResponseDto mapToAllStoresResponseDto(Store store) {
        return new AllStoresResponseDto(store.getId(), store.getName(), store.getLocation());
    }

    public ProductResponseDto mapToProductResponseDto(Product product) {
        return new ProductResponseDto(product.getId(), product.getName(), product.getPrice(), product.getCategory());
    }

}
