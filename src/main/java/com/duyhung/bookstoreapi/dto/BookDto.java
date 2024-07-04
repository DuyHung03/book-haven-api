package com.duyhung.bookstoreapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String bookId;
    private String title;
    private String isbn;
    private int publicationYear;
    private String description;
    private String price;
    private int pageCount;
    private String authorName;
    private List<String> genreNameList;
    private List<String> imgUrls;
    private int quantityInStock;
}
