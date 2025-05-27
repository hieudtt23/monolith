package com.danghieu99.monolith.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class SaveProductImagesRequest {

    @NotBlank
    private final String productUUID;

    @NotEmpty
    @Size(max = 20)
    private final List<@NotNull MultipartFile> imgFiles;
}