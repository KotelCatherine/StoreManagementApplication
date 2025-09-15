package com.example.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO с основной информацией о магазине")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreProductResponseDto {

    @Schema(description = "Идентификатор магазина")
    private UUID id;

    @Schema(description = "Название магазина")
    private UUID storeId;

    @Schema(description = "Местоположение магазина")
    private UUID productId;

}
