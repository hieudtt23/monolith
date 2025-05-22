package com.danghieu99.monolith.product.dto.response;

import com.danghieu99.monolith.product.constant.EProductStatus;
import com.danghieu99.monolith.product.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class ProductDetailsResponse {

    @NotNull
    private String uuid;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private EProductStatus status;

    @NotNull
    private Shop shop;

    @NotEmpty
    private List<@NotNull Category> categories;

    @NotEmpty
    private List<@NotNull VariantDetailsResponse> variants;

    @NotEmpty
    private List<@NotBlank String> imageUrls;
}
