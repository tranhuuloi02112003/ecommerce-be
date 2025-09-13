package com.lh.ecommerce.dto.response;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
    UUID id,
    String name,
    String description,
    Instant createdAt,
    Instant updatedAt,
    UUID createdBy,
    UUID updatedBy) {}
