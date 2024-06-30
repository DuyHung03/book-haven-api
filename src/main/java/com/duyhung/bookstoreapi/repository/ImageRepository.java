package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<BookImage, String> {
}
