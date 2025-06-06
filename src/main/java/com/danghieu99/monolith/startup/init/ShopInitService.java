package com.danghieu99.monolith.startup.init;

import com.danghieu99.monolith.product.constant.EShopStatus;
import com.danghieu99.monolith.product.entity.jpa.Shop;
import com.danghieu99.monolith.product.repository.jpa.ShopRepository;
import com.danghieu99.monolith.security.constant.ERole;
import com.danghieu99.monolith.security.repository.jpa.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShopInitService {

    private final ShopRepository shopRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void init() {
        if (shopRepository.findAll().isEmpty()) {
            var sellers = accountRepository.findByERole(ERole.ROLE_SELLER);
            Set<Shop> shops = new HashSet<>();
            sellers.forEach(seller -> {
                shops.add(Shop.builder()
                        .name("Default shop " + seller.getPhone())
                        .description("Default shop description " + seller.getPhone())
                        .accountUUID(seller.getUuid().toString())
                        .status(seller.getId() % 5 != 0 ? EShopStatus.SHOP_ACTIVE : EShopStatus.SHOP_INACTIVE)
                        .build());
            });
            shopRepository.saveAll(shops);
        }
    }
}
