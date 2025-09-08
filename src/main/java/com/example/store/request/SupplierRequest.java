package com.example.store.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Schema(description = "DTO запроса создания поставщика")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {

    @Schema(description = "Название поставщика")
    @JsonProperty("name")
    @NotBlank
    private String name;

    @Schema(description = "Электронная почта поставщика")
    @JsonProperty("email")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Номер телефона поставщика")
    @JsonProperty("phone")
    private String phone;

    @Schema(description = "Адрес поставщика")
    @JsonProperty("address")
    private String address;

    @Schema(description = "Сайт поставщика")
    @JsonProperty("website")
    @URL(message = "Website должен быть валидным URL")
    private String website;

}
