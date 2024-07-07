package com.duyhung.bookstoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long addressId;
    private String email;
    private String fullName;
    private String phone;
    private int provinceId;
    private String provinceName;
    private int districtId;
    private String districtName;
    private int wardCode;
    private String wardName;
    private String houseNumber;
}
