package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.dto.OrderDto;
import com.duyhung.bookstoreapi.entity.ApiResponse;
import com.duyhung.bookstoreapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<?> saveOrder(
            @RequestParam String userId,
            @RequestBody OrderDto orderDto
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", orderService.saveOrder(orderDto, userId)
        ));
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<?> getOrdersByUserId(
            @RequestParam String userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", orderService.getOrderByUser(userId)
        ));
    }

}
