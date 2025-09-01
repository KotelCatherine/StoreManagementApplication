package com.example.store.mapper;

import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {
    public StoreResponseDto mapToStoreResponseDto(Store store) {
        return new StoreResponseDto(store.getId(), store.getName(), store.getLocation());
    }
}
