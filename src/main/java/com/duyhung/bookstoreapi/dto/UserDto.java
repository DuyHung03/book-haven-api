package com.duyhung.bookstoreapi.dto;

import com.duyhung.bookstoreapi.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String email;
    private String phone;
    private String address;
    private String photoUrl;
    private Role role;
}
