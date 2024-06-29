package com.duyhung.bookstoreapi.entity;

import com.duyhung.bookstoreapi.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UserDto user;
    private String jwtToken;
}
