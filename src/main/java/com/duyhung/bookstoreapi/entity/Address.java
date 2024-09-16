package com.duyhung.bookstoreapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue
    private Long addressId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String email;
    private String fullName;
    private String phone;
    private int provinceId;
    private int districtId;
    private int wardCode;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String houseNumber;

}
