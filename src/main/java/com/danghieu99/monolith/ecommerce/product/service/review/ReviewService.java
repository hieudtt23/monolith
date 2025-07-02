package com.danghieu99.monolith.ecommerce.product.service.review;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.ecommerce.product.dto.request.PostReviewRequest;
import com.danghieu99.monolith.ecommerce.product.dto.response.GetReviewResponse;
import com.danghieu99.monolith.ecommerce.product.dto.response.PostReviewResponse;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.Review;
import com.danghieu99.monolith.ecommerce.product.entity.jpa.ReviewImage;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ProductRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ReviewImageRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.ReviewRepository;
import com.danghieu99.monolith.ecommerce.product.repository.jpa.VariantRepository;
import com.danghieu99.monolith.ecommerce.product.service.image.ImageService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageService imageService;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final ReviewImageRepository reviewImageRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         @Qualifier("product-cloudinary-image-service") ImageService imageService,
                         ProductRepository productRepository,
                         VariantRepository variantRepository,
                         ReviewImageRepository reviewImageRepository) {
        this.reviewRepository = reviewRepository;
        this.imageService = imageService;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.reviewImageRepository = reviewImageRepository;
    }

    @Transactional
    public PostReviewResponse post(final PostReviewRequest request) {
        Review review = Review.builder()
                .productId(productRepository.findByUuid(UUID.fromString(request.getProductUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", request.getProductUUID()))
                        .getId())
                .variantId(variantRepository.findByUuid(UUID.fromString(request.getVariantUUID()))
                        .orElseThrow(() -> new ResourceNotFoundException("Variant", "uuid", request.getVariantUUID()))
                        .getId())
                .rating(request.getRating())
                .content(request.getContent())
                .build();
        int reviewId = reviewRepository.save(review).getId();
        Collection<String> failedUploads = new ConcurrentLinkedQueue<>();
        Collection<ReviewImage> reviewImages = new ConcurrentLinkedQueue<>();
        request.getImages().parallelStream().forEach(file -> {
            String token = UUID.randomUUID().toString();
            try {
                imageService.upload(token, file.getBytes());
                reviewImages.add(ReviewImage.builder()
                        .reviewId(reviewId)
                        .imageToken(token)
                        .build());
            } catch (IOException e) {
                failedUploads.add(file.getName());
                log.error("Upload failed for file: {}, exception: {}", file.getName(), e.getMessage());
            }
        });
        reviewImageRepository.saveAll(reviewImages);
        return PostReviewResponse.builder()
                .message("Post review success!")
                .failedImages(failedUploads)
                .build();
    }

    public Page<GetReviewResponse> getReviewsByProductUUID(@NotBlank String productUUID, @NotNull Pageable pageable) {
        int productId = productRepository.findByUuid(UUID.fromString(productUUID))
                .orElseThrow(() -> new ResourceNotFoundException("Product", "uuid", productUUID))
                .getId();
        Page<Review> reviews = reviewRepository.findByProductId(productId, pageable);
        return reviews.map(this::getReviewResponseFromReview);
    }

    protected GetReviewResponse getReviewResponseFromReview(@NotNull Review review) {
        return GetReviewResponse.builder()
                .rating(review.getRating())
                .content(review.getContent())
                .imageTokens(reviewImageRepository.findByReviewId(review.getId()).stream()
                        .map(ReviewImage::getImageToken)
                        .toList())
                .build();
    }
}