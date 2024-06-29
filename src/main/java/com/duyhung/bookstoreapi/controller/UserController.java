package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.entity.ApiResponse;
import com.duyhung.bookstoreapi.entity.AuthRequest;
import com.duyhung.bookstoreapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) throws Exception {
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), null, userService.register(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.login(request)));
    }

}
