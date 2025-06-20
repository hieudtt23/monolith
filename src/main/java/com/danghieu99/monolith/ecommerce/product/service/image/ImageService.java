package com.danghieu99.monolith.ecommerce.product.service.image;

import java.util.concurrent.CompletableFuture;

public interface ImageService {

    CompletableFuture<?> upload(String token, byte[] byteArray);

    CompletableFuture<?> uploadAndReturnUrl(String token, byte[] byteArray);

    CompletableFuture<?> deleteImage(String token);
}
