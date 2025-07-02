package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import lombok.*;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostReviewResponse extends BaseResponse {

    private String message;

    private Collection<String> failedImages;

}
