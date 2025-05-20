package com.danghieu99.monolith.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class SaveVariantRequest {

    @NotEmpty
    private Map<String, String> attributes;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    private List<MultipartFile> images;
}
