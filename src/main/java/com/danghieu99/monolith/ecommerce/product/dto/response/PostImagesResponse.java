package com.danghieu99.monolith.ecommerce.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(access = AccessLevel.PUBLIC)
public class PostImagesResponse extends BaseResponse {

    @NotNull
    private boolean success;

    private String message;

    private String[] failedFileNames;
}
