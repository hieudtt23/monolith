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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
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
            shopRepository.findAll().parallelStream().forEach(shop -> {
                IntStream.range(0, 10).parallel().forEach(i -> {
                    var savedProduct = productRepository.save(Product.builder()
                            .name("Default product " + UUID.randomUUID())
                            .description("Default product description " + UUID.randomUUID())
                            .status(EProductStatus.LISTED)
                            .basePrice(BigDecimal.valueOf(200))
                            .build());
                    pShopRepository.save(ProductShop.builder()
                            .productId(savedProduct.getId())
                            .shopId(shop.getId())
                            .build());
                    IntStream.range(0, 4).parallel().forEach(j -> {
                        pCategoryRepository.save(ProductCategory.builder()
                                .productId(savedProduct.getId())
                                .categoryId(j).build());
                    });

                    IntStream.range(0, 3).parallel().forEach(k -> {
                        var savedAttribute = attributeRepository.save(Attribute.builder()
                                .productId(savedProduct.getId())
                                .type("Default type " + UUID.randomUUID())
                                .value("Default value " + UUID.randomUUID())
                                .build());

                        IntStream.range(1, 4).parallel().forEach(l -> {
                            var savedVariant = variantRepository.save(Variant.builder()
                                    .stock(l)
                                    .price(BigDecimal.valueOf(300))
                                    .productId(savedProduct.getId())
                                    .build());

                            IntStream.range(0, 3).parallel().forEach(m -> {
                                vAttributeRepository.save(VariantAttribute.builder()
                                        .attributeId(savedAttribute.getId())
                                        .variantId(savedVariant.getId())
                                        .attributeType(savedAttribute.getType())
                                        .build());
                            });
                        });
                    });
                });
            });
        }
    }
}