package com.danghieu99.monolith.product.service.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.danghieu99.monolith.common.validate.ValidMultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CloudinaryImageService {

    private final Cloudinary cloudinary;

    public CloudinaryImageService(@Qualifier("product-cloudinary") Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(@ValidMultipartFile() MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", "product-image_" + UUID.randomUUID(),
                "eager", Collections.singletonList(new Transformation().width(1920).height(1080).crop("limit").quality("auto").fetchFormat("auto"))
                , "resource_type", "image"
        ));
        return (String) uploadResult.get("url");
    }
}