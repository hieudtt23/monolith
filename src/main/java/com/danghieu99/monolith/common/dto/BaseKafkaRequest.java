package com.danghieu99.monolith.common.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseKafkaRequest extends BaseRequest {

    private String systemCode;
}
