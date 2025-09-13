package com.lh.ecommerce.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    String name,
    String description,
    Double price,
    UUID categoryId,
    Instant createdAt,
    Instant updatedAt,
    UUID createdBy,
    UUID updatedBy) {}
