package com.danghieu99.monolith.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SaveProductRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotBlank
    private final List<String> categories;

    @NotNull
    private final BigDecimal price;

    @NotEmpty
    private final List<SaveVariantRequest> variants;

    private final List<MultipartFile> images;
}
