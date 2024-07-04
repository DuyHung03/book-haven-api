package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.entity.ApiResponse;
import com.duyhung.bookstoreapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;


    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam String userId,
            @RequestParam String bookId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", cartService.addToCart(userId, bookId, quantity)
        ));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getItemsFromCart(@RequestParam String userId) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", cartService.getItemsFromCart(userId)
        ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBookFromCart(
            @RequestParam String bookId,
            @RequestParam String userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", cartService.deleteItemFromCart(bookId, userId)
        ));
    }

    @PutMapping("/increaseQuantity")
    public ResponseEntity<?> increaseQuantity(
            @RequestParam String bookId,
            @RequestParam String userId
    ){
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", cartService.increaseQuantity(bookId, userId)
        ));
    }

    @PutMapping("/decreaseQuantity")
    public ResponseEntity<?> decreaseQuantity(
            @RequestParam String bookId,
            @RequestParam String userId
    ){
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", cartService.decreaseQuantity(bookId, userId)
        ));
    }

}
