package com.lh.ecommerce.controller;

import com.lh.ecommerce.dto.response.AddressResponse;
import com.lh.ecommerce.dto.resquest.AddressRequest;
import com.lh.ecommerce.service.address.AddressService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

  private final AddressService addressService;

  @GetMapping
  public List<AddressResponse> getAllAddresses() {
    return addressService.getAllAddresses();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void addAddress(@Valid @RequestBody AddressRequest request) {
    addressService.addAddress(request);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateAddress(@PathVariable UUID id, @Valid @RequestBody AddressRequest request) {
    addressService.updateAddress(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAddress(@PathVariable UUID id) {
    addressService.deleteAddress(id);
  }

  @PutMapping("/{id}/default")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setDefaultAddress(@PathVariable UUID id) {
    addressService.setDefaultAddress(id);
  }
}
