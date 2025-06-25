package com.danghieu99.monolith.ecommerce.product.service.shop;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.dto.request.SaveShopRequest;
import com.danghieu99.monolith.ecommerce.product.dto.request.UpdateShopDetailsRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.ShopDetailsResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Shop;
import com.danghieu99.monolith.ecommerce.product.mapper.ShopMapper;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ShopRepository;
import com.danghieu99.monolith.security.config.auth.UserDetailsImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerShopService {

    private final ShopMapper shopMapper;
    private final ShopRepository shopRepository;

    @Transactional
    public ShopDetailsResponse createCurrentUserShop(@NotNull UserDetailsImpl userDetails,
                                                     @NotNull SaveShopRequest request) {
        var newShop = shopMapper.toShop(request);
        newShop.setAccountUUID(UUID.fromString(userDetails.getUuid()));
        return shopMapper.toResponse(shopRepository.save(newShop));
    }

    @Transactional
    public void deleteCurrentUserShop(@NotNull UserDetailsImpl userDetails) {
        shopRepository.deleteByAccountUUID(UUID.fromString(userDetails.getUuid()));
    }

    @Transactional
    public ShopDetailsResponse editCurrentUserShopDetails(@NotNull UserDetailsImpl userDetails,
                                                          @NotNull UpdateShopDetailsRequest request) {
        Shop shop = shopRepository.findByAccountUUID(UUID.fromString(userDetails.getUuid()))
                .orElseThrow(() -> new ResourceNotFoundException("Shop", "accountUUID", userDetails.getUuid()));
        if (request.getName() != null && !request.getName().isEmpty()) {
            shop.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            shop.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            shop.setStatus(request.getStatus());
        }
        var updatedShop = shopRepository.save(shop);
        return shopMapper.toResponse(updatedShop);
    }
}