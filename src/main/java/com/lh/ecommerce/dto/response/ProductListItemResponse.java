package com.lh.ecommerce.dto.response;

import java.util.UUID;

public record ProductListItemResponse(
    UUID id, String name, String description, Double price, String categoryName, String imageUrl) {}
