package com.example.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "DTO с основной информацией о магазине")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    @Schema(description = "Идентификатор магазина")
    private UUID id;

    @Schema(description = "Название магазина")
    private String name;

    @Schema(description = "Местоположение магазина")
    private BigDecimal price;

    @Schema(description = "Электронная почта магазина")
    private String category;

}
