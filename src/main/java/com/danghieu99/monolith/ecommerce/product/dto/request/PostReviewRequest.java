package com.danghieu99.monolith.ecommerce.product.dto.request;

import com.danghieu99.monolith.common.dto.BaseRequest;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class PostReviewRequest extends BaseRequest {

    @NotBlank
    private final String productUUID;

    @NotBlank
    private final String variantUUID;

    @NotNull
    @Min(1)
    @Max(5)
    private final int rating;

    @NotBlank
    private final String content;

    @NotEmpty
    private final Collection<@NotNull MultipartFile> images;
}
