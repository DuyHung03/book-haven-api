package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/email/changePassword")
    public ResponseEntity<?> sendEmailChangePassword(@RequestParam String email) throws Exception {
        return ResponseEntity.ok().body(emailService.sendEmailVerifyCode(email));
    }

}
