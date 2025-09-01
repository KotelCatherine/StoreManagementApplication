package com.example.store.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("name")
    @NotBlank
    private String name;

    @Schema(description = "Местоположение магазина")
    @JsonProperty("location")
    @NotBlank
    private String location;

}
