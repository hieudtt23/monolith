package com.danghieu99.monolith.ecommerce.product.dto.response;


import com.danghieu99.monolith.common.dto.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class GetReviewResponse extends BaseResponse {

    private final int rating;

    @NotBlank
    private final String content;

    @NotEmpty
    private Collection<@NotBlank String> imageTokens;
}