package com.example.store.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO запроса создания магазина")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {

    @Schema(description = "Название магазина")
    @NotBlank
    private String name;

    @Schema(description = "Местоположение магазина")
    @NotBlank
    private String location;

}
