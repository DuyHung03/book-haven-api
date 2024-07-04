package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByBookBookIdAndCartCartId(String bookId, Long cartId);
}
