package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.entity.VerifyCode;
import com.duyhung.bookstoreapi.repository.VerifyCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyCodeService {

    private final VerifyCodeRepository verifyCodeRepository;

    public boolean validateCode(String code) {
        VerifyCode entity = verifyCodeRepository.findByCode(code);
        if (entity == null) {
            return false;
        }
        return !entity.isExpired();
    }

}
