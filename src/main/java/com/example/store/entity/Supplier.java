package com.example.store.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "suppliers")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@EnableJpaAuditing
public class Supplier {

    @Id
    private UUID id;

    @NotBlank
    @Column
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @URL
    @Column
    private String website;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
