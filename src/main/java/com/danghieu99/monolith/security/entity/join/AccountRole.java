package com.danghieu99.monolith.security.entity.join;

import com.danghieu99.monolith.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(name = "account_roles",
        uniqueConstraints = {@UniqueConstraint(name = "uq_account_role",
                columnNames = "account_id, role_id")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AccountRole extends BaseEntity {

    @Column(nullable = false)
    private int accountId;

    @Column(nullable = false)
    private int roleId;
}
