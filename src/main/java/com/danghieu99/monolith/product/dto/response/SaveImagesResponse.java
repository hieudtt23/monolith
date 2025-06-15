package com.danghieu99.monolith.product.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(access = AccessLevel.PUBLIC)
public class SaveImagesResponse {

    @NotNull
    private boolean success;

    private String message;

    private String[] failedFileNames;
}
