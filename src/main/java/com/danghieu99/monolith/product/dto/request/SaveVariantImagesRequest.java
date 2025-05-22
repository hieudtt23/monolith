package com.danghieu99.monolith.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class SaveVariantImagesRequest {

    @NotBlank
    private final String variantUUID;

    @NotEmpty
    private List<@NotNull MultipartFile> variantImgFiles;
}