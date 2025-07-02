package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GetProductDetailsResponse extends BaseResponse {

    @NotNull
    private String uuid;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private long basePrice;

    @NotNull
    private String status;

    @NotNull
    private String shopUUID;

    private List<@NotBlank String> categories;

    private List<@NotNull GetVariantDetailsResponse> variants;

    private List<@NotBlank String> imageTokens;

    private Page<@NotNull GetReviewResponse> reviews;
}