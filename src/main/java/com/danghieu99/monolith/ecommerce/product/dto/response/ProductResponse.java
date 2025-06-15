package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.ecommerce.product.constant.EProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    @NotNull
    private String uuid;

    @NotBlank
    private String name;

    @NotEmpty
    private String imageToken;

    @NotNull
    private EProductStatus status;
}
