package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
}
