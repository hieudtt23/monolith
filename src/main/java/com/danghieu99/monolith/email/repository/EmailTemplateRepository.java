package com.danghieu99.monolith.email.repository;

import com.danghieu99.monolith.email.entity.EmailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {

    Optional<EmailTemplate> findByUuid(UUID uuid);

    Optional<EmailTemplate> findByName(final String name);

    @Query("select e from EmailTemplate e " +
            "join EmailTemplateCategory etc " +
            "on e.categoryId = etc.id " +
            "where etc.name = :categoryName")
    List<EmailTemplate> findByCategory(final String categoryName);

    @Query("select e from EmailTemplate e " +
            "join EmailTemplateCategory etc " +
            "on e.categoryId = etc.id " +
            "where etc.name = :categoryName")
    Page<EmailTemplate> findByCategory(final String categoryName, Pageable pageable);
}
