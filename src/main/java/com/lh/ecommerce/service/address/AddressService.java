package com.lh.ecommerce.service.address;

import com.lh.ecommerce.dto.response.AddressResponse;
import com.lh.ecommerce.dto.resquest.AddressRequest;
import com.lh.ecommerce.entity.AddressEntity;
import com.lh.ecommerce.mapper.AddressMapper;
import com.lh.ecommerce.repository.AddressRepository;
import com.lh.ecommerce.utils.SecurityUtils;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

  private static final int MAX_ADDRESSES_PER_USER = 4;
  private final AddressRepository addressRepository;
  private final AddressMapper addressMapper;

  public List<AddressResponse> getAllAddresses() {
    UUID userId = SecurityUtils.getCurrentUserId();
    List<AddressEntity> addresses = addressRepository.findByUserId(userId);
    return addressMapper.toResponse(addresses);
  }

  @Transactional
  public void addAddress(AddressRequest request) {
    UUID userId = SecurityUtils.getCurrentUserId();
    List<AddressEntity> existing = addressRepository.findByUserId(userId);

    if (existing.size() >= MAX_ADDRESSES_PER_USER) {
      throw AddressError.addressLimitReached().get();
    }

    AddressEntity entity = addressMapper.toEntity(request, userId);

    if (existing.isEmpty()) {
      entity.setDefaultAddress(true);
    }

    addressRepository.save(entity);
  }

  @Transactional
  public void updateAddress(UUID id, AddressRequest request) {
    UUID userId = SecurityUtils.getCurrentUserId();
    AddressEntity addressEntity =
        addressRepository.findByIdAndUserId(id, userId).orElseThrow(AddressError.addressNotFound());

    addressMapper.updateEntity(request, addressEntity);
    addressRepository.save(addressEntity);
  }

  @Transactional
  public void deleteAddress(UUID id) {
    UUID userId = SecurityUtils.getCurrentUserId();

    AddressEntity address =
        addressRepository.findByIdAndUserId(id, userId).orElseThrow(AddressError.addressNotFound());

    if (address.isDefaultAddress()) {
      throw AddressError.cannotDeleteDefaultAddress().get();
    }

    addressRepository.deleteByIdAndUserId(id, userId);
  }

  @Transactional
  public void setDefaultAddress(UUID id) {
    UUID userId = SecurityUtils.getCurrentUserId();
    if (!addressRepository.existsByIdAndUserId(id, userId)) {
      throw AddressError.addressNotFound().get();
    }

    addressRepository.clearDefaultByUserId(userId);
    addressRepository.flush();

    AddressEntity address =
        addressRepository.findById(id).orElseThrow(AddressError.addressNotFound());
    address.setDefaultAddress(true);
    addressRepository.save(address);
  }
}
