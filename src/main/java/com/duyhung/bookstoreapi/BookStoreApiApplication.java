package com.duyhung.bookstoreapi;

import com.duyhung.bookstoreapi.entity.Author;
import com.duyhung.bookstoreapi.entity.Book;
import com.duyhung.bookstoreapi.entity.Genre;
import com.duyhung.bookstoreapi.repository.AuthorRepository;
import com.duyhung.bookstoreapi.repository.BookRepository;
import com.duyhung.bookstoreapi.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class BookStoreApiApplication implements CommandLineRunner {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
