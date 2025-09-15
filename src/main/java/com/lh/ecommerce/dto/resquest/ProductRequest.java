package com.lh.ecommerce.dto.resquest;

import java.util.List;
import java.util.UUID;

public record ProductRequest(
    String name, String description, double price, UUID categoryId, List<String> imageUrls) {}
