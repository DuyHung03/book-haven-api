package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.Book;
import com.duyhung.bookstoreapi.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query(nativeQuery = true, value = "select * from books where title like concat('%', :bookName, '%')")
    List<Book> findAllByName(@Param("bookName") String bookName);

    @Query(value = "SELECT TOP (:range) * FROM books ORDER BY NEWID()", nativeQuery = true)
    List<Book> findRandomBooks(@Param("range") int range);

    List<Book> findByAuthorAuthorName(String authorName);

    List<Book> findByGenres(List<Genre> genre);

}

