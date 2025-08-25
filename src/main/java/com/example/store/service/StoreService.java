package com.example.store.service;

import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public StoreResponseDto createStore(StoreRequest request) {

        Store store = new Store(UUID.randomUUID(), request.getName(), request.getLocation(), null);

        storeRepository.saveAndFlush(store);

        return new StoreResponseDto(store.getId(), store.getName(), store.getLocation());

    }

}
