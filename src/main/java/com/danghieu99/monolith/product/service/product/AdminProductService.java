package com.danghieu99.monolith.product.service.product;

import com.danghieu99.monolith.product.repository.jpa.AttributeRepository;
import com.danghieu99.monolith.product.repository.jpa.ProductRepository;
import com.danghieu99.monolith.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.product.repository.jpa.join.ProductCategoryRepository;
import com.danghieu99.monolith.product.repository.jpa.join.ProductShopRepository;
import com.danghieu99.monolith.product.repository.jpa.join.VariantAttributeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductShopRepository productShopRepository;
    private final VariantRepository variantRepository;
    private final AttributeRepository attributeRepository;
    private final VariantAttributeRepository variantAttributeRepository;

    @Transactional
    public void deleteProductById(@NotNull Integer id) {
        productRepository.deleteById(id);
        productCategoryRepository.deleteByProductId(id);
        productShopRepository.deleteByProductId(id);
        variantRepository.deleteByProductId(id);
    }


    @Transactional
    public void deleteAttributeById(@NotBlank int attributeId) {
        attributeRepository.deleteById(attributeId);
        variantAttributeRepository.deleteByAttributeId(attributeId);
    }

}