package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {

    Optional<Genre> findByGenreName(String genreName);

}
