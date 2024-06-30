package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.dto.BookDto;
import com.duyhung.bookstoreapi.entity.ApiResponse;
import com.duyhung.bookstoreapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BookController {

    private final BookService bookService;

    @PostMapping("/book/add")
    public ResponseEntity<?> addNewBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", bookService.addNewBook(bookDto)));
    }

    @GetMapping("/book/search")
    public ResponseEntity<?> getBookListByName(@RequestParam String bookName) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getBooksByName(bookName)
        ));
    }

    @GetMapping("/book/getRandom")
    public ResponseEntity<?> getRandomBooks(@RequestParam int range) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getRandomBooks(range)
        ));
    }

    @GetMapping("/book/getByAuthor")
    public ResponseEntity<?> getBooksByAuthor(@RequestParam String authorName) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getBookByAuthor(authorName)
        ));
    }

    @GetMapping("/book/getByGenre")
    public ResponseEntity<?> getBooksByGenre(@RequestParam String genreName) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getBooksByGenre(genreName)
        ));
    }
}
