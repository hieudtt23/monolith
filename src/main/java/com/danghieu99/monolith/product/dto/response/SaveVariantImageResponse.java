package com.danghieu99.monolith.product.dto.response;

import com.danghieu99.monolith.common.dto.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(access = AccessLevel.PUBLIC)
public class SaveVariantImageResponse extends BaseResponse {

    //variantUUID:imageToken
    @NotEmpty
    private Map<@NotBlank String, @NotBlank String> failedMap;
}
