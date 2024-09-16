package com.duyhung.bookstoreapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long cartItemId;
    private String bookId;
    private String title;
    private String authorName;
    private List<String> imgUrls;
    private int quantity;
    private String price;
}
