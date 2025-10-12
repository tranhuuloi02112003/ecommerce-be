package com.lh.ecommerce.dto.resquest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record ProductRequest(
    @NotBlank String name,
    @NotBlank String description,
    @NotNull(message = "Price is required") double price,
    @NotNull(message = "Quantity is required") int quantity,
    @NotNull(message = "CategoryId is required") UUID categoryId,
    @NotEmpty(message = "Image must not be empty") List<@NotBlank String> imageKeys) {}
