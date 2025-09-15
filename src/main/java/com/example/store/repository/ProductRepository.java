package com.example.store.repository;

import com.example.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product,UUID> {
}
