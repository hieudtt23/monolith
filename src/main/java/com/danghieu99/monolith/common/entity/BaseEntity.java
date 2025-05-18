package com.danghieu99.monolith.common.entity;

import lombok.AccessLevel;
import lombok.Getter;

import jakarta.persistence.*;

import jakarta.persistence.MappedSuperclass;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false, insertable = false)
    private Integer id;

    @Setter(AccessLevel.NONE)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @PrePersist
    private void prePersist() {
        this.uuid = UUID.randomUUID();
    }

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;
}