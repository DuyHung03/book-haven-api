package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.Book;
import com.duyhung.bookstoreapi.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    @Query(value = "SELECT * FROM books WHERE LOWER(title) LIKE '%' + LOWER(:bookName) + '%'", nativeQuery = true)
    Page<Book> findAllByName(@Param("bookName") String bookName, Pageable pageable);
    @Query(value = "SELECT TOP (:range) * FROM books ORDER BY NEWID()", nativeQuery = true)
    List<Book> findRandomBooks(@Param("range") int range);

    List<Book> findByAuthorAuthorName(String authorName);

    Page<Book> findByGenres(List<Genre> genre, Pageable pageable);

}

