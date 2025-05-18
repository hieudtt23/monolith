package com.danghieu99.monolith.order.entity;

import com.danghieu99.monolith.common.entity.BaseEntity;
import com.danghieu99.monolith.order.constant.ECancelStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cancel_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CancelRequest extends BaseEntity {

    @Column(nullable = false)
    private UUID userAccountUUID;

    @Column(nullable = false)
    private UUID orderUUID;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    @Builder.Default
    private ECancelStatus status = ECancelStatus.PENDING;
}