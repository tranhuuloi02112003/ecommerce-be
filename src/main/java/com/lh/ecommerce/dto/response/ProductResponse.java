package com.lh.ecommerce.dto.response;

import java.util.List;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String name,
    String description,
    Double price,
    UUID categoryId,
    List<String> imageUrls,
    List<UUID> colorIds,
    List<UUID> sizeIds) {}
