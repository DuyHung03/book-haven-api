package com.duyhung.bookstoreapi.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long cartId;
    private String userId;
    private List<CartItemDto> cartItems;

}
