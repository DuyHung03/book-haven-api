package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.entity.Author;
import com.duyhung.bookstoreapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author addNewAuthor(Author author){
        return authorRepository.save(author);
    }

}
