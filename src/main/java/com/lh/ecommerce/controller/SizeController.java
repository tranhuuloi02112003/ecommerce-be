package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.SizeResponse;
import com.lh.ecommerce.dto.resquest.SizeRequest;
import com.lh.ecommerce.service.size.SizeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sizes")
public class SizeController {
  private final SizeService sizeService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SizeResponse create(@RequestBody SizeRequest request) {
    return sizeService.create(request);
  }

  @GetMapping
  public List<SizeResponse> getAll() {
    return sizeService.findAll();
  }
}
