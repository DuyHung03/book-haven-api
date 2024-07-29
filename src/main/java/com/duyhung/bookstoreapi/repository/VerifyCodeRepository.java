package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {

    VerifyCode findByCode(String code);

}
