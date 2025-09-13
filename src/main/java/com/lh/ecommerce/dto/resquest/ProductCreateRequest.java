package com.lh.ecommerce.dto.resquest;

import java.util.UUID;

public record ProductCreateRequest(
    String name, String description, double price, UUID categoryId) {}
