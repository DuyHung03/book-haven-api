package com.duyhung.bookstoreapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long cartItemId;
    private String bookId;
    private String title;
    private String price;
    private String authorName;
    private List<String> imgUrls;
    private int quantity;
    private int inStock;
}
