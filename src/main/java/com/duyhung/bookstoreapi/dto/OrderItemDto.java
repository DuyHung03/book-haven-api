package com.duyhung.bookstoreapi.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private String bookId;
    private String title;
    private String authorName;
    private List<String> imgUrls;
    private int quantity;
    private String price;
}
