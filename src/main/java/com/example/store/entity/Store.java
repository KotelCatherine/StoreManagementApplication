package com.example.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "stores")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Store {

    @Id
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private String location;


    @LastModifiedDate
    private LocalDateTime updatedAt;

}
