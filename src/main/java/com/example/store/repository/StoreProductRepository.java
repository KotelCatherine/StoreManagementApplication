package com.example.store.repository;

import com.example.store.entity.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreProductRepository extends JpaRepository<StoreProduct, UUID> {
    List<StoreProduct> findByStoreId(UUID id);
}
