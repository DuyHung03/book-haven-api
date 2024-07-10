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
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewBook(@Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", bookService.addNewBook(bookDto)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getBookListByName(
            @RequestParam String bookName,
            @RequestParam int pageNo,
            @RequestParam int pageSize

    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getBooksByName(bookName, pageNo, pageSize)
        ));
    }

    @GetMapping("/getRandom")
    public ResponseEntity<?> getRandomBooks(@RequestParam int range) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getRandomBooks(range)
        ));
    }

    @GetMapping("/getByAuthor")
    public ResponseEntity<?> getBooksByAuthor(@RequestParam String authorName) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getBookByAuthor(authorName)
        ));
    }

    @GetMapping("/getByGenre")
    public ResponseEntity<?> getBooksByGenre(
            @RequestParam String genreName,
            @RequestParam int pageNo,
            @RequestParam int pageSize
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                bookService.getBooksByGenre(genreName, pageNo, pageSize)
        ));
    }
}
