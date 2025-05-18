package com.danghieu99.monolith.order.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.order.constant.EReturnStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "returns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Return extends BaseEntity {

    @Column(nullable = false)
    private int orderItemId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EReturnStatus status;
}