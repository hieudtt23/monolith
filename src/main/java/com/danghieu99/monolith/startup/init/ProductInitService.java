package com.danghieu99.monolith.startup.init;

import com.danghieu99.monolith.ecommerce.product.constant.EProductStatus;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Attribute;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Product;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Variant;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.join.ProductCategory;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.join.ProductShop;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.join.VariantAttribute;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.AttributeRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ProductRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ShopRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.ProductCategoryRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.ProductShopRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.join.VariantAttributeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductInitService {

    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final ProductShopRepository pShopRepository;
    private final AttributeRepository attributeRepository;
    private final ProductCategoryRepository pCategoryRepository;
    private final VariantAttributeRepository vAttributeRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public void init() {
        if (productRepository.findAll().isEmpty()) {
            shopRepository.findAll().forEach(shop -> {
                var savedProduct = productRepository.save(Product.builder()
                        .name("Default product " + shop)
                        .description("Default product description " + shop)
                        .status(EProductStatus.LISTED)
                        .basePrice(BigDecimal.valueOf(Math.random()))
                        .build());
                pShopRepository.save(ProductShop.builder()
                        .productId(savedProduct.getId())
                        .shopId(shop.getId())
                        .build());

                IntStream.range(1, 5).parallel().forEach(j -> {
                    pCategoryRepository.save(pCategoryRepository.save(ProductCategory.builder()
                            .productId(savedProduct.getId())
                            .categoryId(j).build()));
                });

                IntStream.range(1, 5).parallel().forEach(k -> {
                    var attribute = attributeRepository.save(Attribute.builder()
                            .productId(savedProduct.getId())
                            .type("Default type " + k)
                            .value("Default value " + k)
                            .build());

                    IntStream.range(1, 5).parallel().forEach(j -> {
                        var variant = variantRepository.save(Variant.builder()
                                .stock(j)
                                .price(BigDecimal.valueOf(j))
                                .productId(savedProduct.getId())
                                .build());

                        vAttributeRepository.save(vAttributeRepository.save(VariantAttribute.builder()
                                .attributeId(attribute.getId())
                                .variantId(variant.getId())
                                .attributeType(attribute.getType())
                                .build()));
                    });
                });
            });

        }
    }
}