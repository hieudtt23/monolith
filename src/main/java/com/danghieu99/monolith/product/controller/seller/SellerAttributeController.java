package com.danghieu99.monolith.product.controller.seller;

import com.danghieu99.monolith.product.service.product.SellerProductService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller/attribute")
@Validated
public class SellerAttributeController {

    private final SellerProductService sellerProductService;

    @DeleteMapping("")
    public void deleteAttributeByProductUUIDTypeValue(@RequestParam @NotBlank String productUUID,
                                                      @RequestParam @NotBlank String type,
                                                      @RequestParam @NotBlank String value) {
        sellerProductService.deleteAttributeByProductUUIDTypeValue(productUUID, type, value);
    }

    @DeleteMapping("/by-uuid")
    public void deleteAttributeByUUID(@RequestParam @NotBlank String attributeUUID) {
        sellerProductService.deleteAttributeByUUID(attributeUUID);
    }
}