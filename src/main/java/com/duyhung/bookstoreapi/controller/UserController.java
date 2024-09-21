package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.dto.UserDto;
import com.duyhung.bookstoreapi.entity.ApiResponse;
import com.duyhung.bookstoreapi.entity.AuthRequest;
import com.duyhung.bookstoreapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.login(request, response)));
    }

//    @PostMapping("/auth/user-info")
//    public ResponseEntity<?> oauth2Login(){
//        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.oauth2Login()));
//    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws RuntimeException {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.refreshToken(request, response)));
    }

    @PutMapping("/user/uploadAvatar")
    public ResponseEntity<?> saveAvatar(
            @RequestParam String userId,
            @RequestParam String url
    ) {
        userService.saveAvatarUrl(url, userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", null));

    }

    @PostMapping("/verification")
    public ResponseEntity<?> verifyCode(
            @RequestParam String code,
            @RequestParam String userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.verification(code, userId)));
    }

    @PostMapping("/saveOpt")
    public ResponseEntity<?> saveOpt(
            @RequestParam String code
    ){
        return  ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.saveOTP(code)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.resetPassword(token, newPassword)));
    }

    @PostMapping("/user/editInfo")
    public ResponseEntity<?> editInfo(
            @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok().body(new ApiResponse<>(HttpStatus.OK.value(), "Success", userService.saveUserInfo(userDto)));

    }

}
