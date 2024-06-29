package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.BookDto;
import com.duyhung.bookstoreapi.entity.Author;
import com.duyhung.bookstoreapi.entity.Book;
import com.duyhung.bookstoreapi.entity.Genre;
import com.duyhung.bookstoreapi.repository.AuthorRepository;
import com.duyhung.bookstoreapi.repository.BookRepository;
import com.duyhung.bookstoreapi.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public BookDto addNewBook(BookDto bookDto) {
        Author author = authorRepository.findByAuthorName(bookDto.getAuthorName()).orElseThrow(() -> new RuntimeException("Author not found"));

        List<Genre> genres = bookDto.getGenreNameList().stream().map(genreName -> genreRepository.findByGenreName(genreName).orElseThrow(() -> new RuntimeException("Genre not found"))).collect(Collectors.toList());


        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setDescription(bookDto.getDescription());
        book.setPrice(bookDto.getPrice());
        book.setPageCount(bookDto.getPageCount());
        book.setAuthor(author);
        book.setGenres(genres);
        bookRepository.save(book);

        return bookDto;
    }

    public List<BookDto> getBooksByName(String bookName) {
        List<Book> books = bookRepository.findAllByName(bookName);

        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookDto convertToDto(Book book) {
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        if (book.getGenres() != null) {
            bookDto.setGenreNameList(
                    book.getGenres().stream()
                            .map(Genre::getGenreName)
                            .collect(Collectors.toList())
            );
        }

        return bookDto;
    }

    public List<BookDto> getRandomBooks(int range) {
        List<Book> books = bookRepository.findRandomBooks(range);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

}
