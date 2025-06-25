package com.danghieu99.monolith.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderAddressRequest {

    @NotBlank
    private final String name;

    @NotBlank
    private final String phone;

    @NotBlank
    private final String line1;

    private final String line2;

    @NotBlank
    private final String city;

    @NotBlank
    private final String country;

    @NotBlank
    @Pattern(regexp = "^.{5}$|^.{6}$|^.{9}$") //length 5 or 6 or 9
    private final String postalCode;
}