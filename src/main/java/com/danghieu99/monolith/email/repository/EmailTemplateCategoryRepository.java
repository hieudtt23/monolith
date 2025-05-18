package com.danghieu99.monolith.email.repository;

import com.danghieu99.monolith.email.entity.EmailTemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateCategoryRepository extends JpaRepository<EmailTemplateCategory, Integer> {

    Optional<EmailTemplateCategory> findByName(String name);
}