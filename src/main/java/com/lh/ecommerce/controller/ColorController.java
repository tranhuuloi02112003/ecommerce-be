package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.ColorResponse;
import com.lh.ecommerce.dto.resquest.ColorRequest;
import com.lh.ecommerce.service.color.ColorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/colors")
public class ColorController {
  private final ColorService colorService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ColorResponse create(@RequestBody ColorRequest request) {
    return colorService.create(request);
  }

  @GetMapping
  public List<ColorResponse> getAll() {
    return colorService.findAll();
  }
}
