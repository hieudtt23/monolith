package com.danghieu99.monolith.ecommerce.product.service.shop;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetShopDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.mapper.ShopMapper;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ShopRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;

    public GetShopDetailsResponse getByUUID(@NotBlank String uuid) {
        return shopMapper.toResponse(shopRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "uuid", uuid)));
    }

    public GetShopDetailsResponse getByName(@NotBlank String name) {
        return shopMapper.toResponse(shopRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "name", name)));
    }

    public Page<GetShopDetailsResponse> getByNameContaining(@NotBlank String name, @NotNull Pageable pageable) {
        return shopRepository.findByNameContaining(name, pageable).map(shopMapper::toResponse);
    }
}
