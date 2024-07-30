package com.duyhung.bookstoreapi.dto;

import com.duyhung.bookstoreapi.entity.Address;
import com.duyhung.bookstoreapi.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String email;
    private String name;
    private String gender;
    private String phone;
    private String photoUrl;
    private String birthday;
    private AddressDto address;
    private Role role;
}
