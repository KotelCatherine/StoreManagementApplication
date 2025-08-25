package com.example.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "stores")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Store {

    @Id
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private String location;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

}
