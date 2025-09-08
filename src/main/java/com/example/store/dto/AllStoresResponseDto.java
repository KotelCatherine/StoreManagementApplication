package com.example.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO с основной информацией для работы со списком всех магазинов")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllStoresResponseDto {

    @Schema(description = "Идентификатор магазина")
    private UUID id;

    @Schema(description = "Название магазина")
    private String name;

    @Schema(description = "Местоположение магазина")
    private String location;

}