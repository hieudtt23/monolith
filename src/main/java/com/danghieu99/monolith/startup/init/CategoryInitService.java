package com.danghieu99.monolith.startup.init;

import com.danghieu99.monolith.product.entity.jpa.Category;
import com.danghieu99.monolith.product.repository.jpa.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CategoryInitService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void init() {
        if (categoryRepository.findAll().isEmpty()) {
            IntStream.range(1, 50).parallel().forEach(i -> {
                categoryRepository.save(Category.builder()
                        .name("Default Category " + i)
                        .description("Default category description " + i)
                        .build());
            });
        }
    }
}