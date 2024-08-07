package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findByAuthorName(String name);

}
