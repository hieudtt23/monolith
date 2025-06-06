package com.danghieu99.monolith.product.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(access = AccessLevel.PUBLIC)
public class SaveImagesResponse {

    private boolean success;

    private String message;
}
