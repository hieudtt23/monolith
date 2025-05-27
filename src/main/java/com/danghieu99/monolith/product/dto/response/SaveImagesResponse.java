package com.danghieu99.monolith.product.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class SaveImagesResponse {

    private boolean success;

    private String message;
}
