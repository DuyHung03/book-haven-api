package com.duyhung.bookstoreapi.controller;

import com.duyhung.bookstoreapi.dto.AddressDto;
import com.duyhung.bookstoreapi.entity.ApiResponse;
import com.duyhung.bookstoreapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/save")
    public ResponseEntity<?> addNewAddress(@RequestBody AddressDto addressDto,
                                           @RequestParam String userId) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", addressService.saveAddress(addressDto, userId)
        ));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAddress(
            @RequestParam String userId) {
        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success", addressService.getAddress(userId)
        ));
    }

}
