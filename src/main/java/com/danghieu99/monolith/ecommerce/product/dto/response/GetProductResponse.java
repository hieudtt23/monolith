package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import com.danghieu99.monolith.ecommerce.product.constant.EProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GetProductResponse extends BaseResponse {

    @NotNull
    private String uuid;

    @NotBlank
    private String name;

    private String imageToken;

    @NotNull
    private EProductStatus status;
}
