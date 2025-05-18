package com.danghieu99.monolith.email.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "email_template_categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EmailTemplateCategory extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}
