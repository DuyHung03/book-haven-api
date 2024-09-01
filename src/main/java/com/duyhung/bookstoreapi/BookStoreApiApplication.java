package com.duyhung.bookstoreapi;

import com.duyhung.bookstoreapi.entity.*;
import com.duyhung.bookstoreapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableCaching
@EnableWebSecurity
public class BookStoreApiApplication implements CommandLineRunner {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private InventoryRepository inventoryRepository;


    public static void main(String[] args) {
        SpringApplication.run(BookStoreApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//       Random random = new Random();
//        List<Book> bookIds = bookRepository.findAll();
//        for (Book book : bookIds) {
//            int randomQuantity = random.nextInt(30) + 1; // Random quantity between 1 and 100
//
//            Inventory inventory = new Inventory();
//            inventory.setBook(book);
//            inventory.setStock(randomQuantity);
//
//            inventoryRepository.save(inventory);
//        }
    }
}
