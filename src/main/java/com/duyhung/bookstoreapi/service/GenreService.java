package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.entity.Author;
import com.duyhung.bookstoreapi.entity.Book;
import com.duyhung.bookstoreapi.entity.Genre;
import com.duyhung.bookstoreapi.repository.BookRepository;
import com.duyhung.bookstoreapi.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre addNewAuthor(Genre genre){
        return genreRepository.save(genre);
    }


}
