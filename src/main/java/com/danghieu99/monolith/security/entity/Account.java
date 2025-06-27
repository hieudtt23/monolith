package com.danghieu99.monolith.security.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.security.constant.EAccountStatus;
import com.danghieu99.monolith.security.constant.EGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Account extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EGender gender;

    @Column(nullable = false)
    private String fullName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAccountStatus status = EAccountStatus.ACCOUNT_ACTIVE;

    @Builder.Default
    private boolean emailConfirmed = false;

    @Builder.Default
    private boolean phoneConfirmed = false;
}