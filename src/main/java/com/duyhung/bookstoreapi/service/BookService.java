package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.BookDto;
import com.duyhung.bookstoreapi.dto.BooksResponse;
import com.duyhung.bookstoreapi.entity.*;
import com.duyhung.bookstoreapi.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final ImageRepository imageRepository;
    private final InventoryRepository inventoryRepository;
    private final BookRedisService bookRedisService;

    public BookDto addNewBook(BookDto bookDto) {
        Author author = authorRepository.findByAuthorName(bookDto.getAuthorName())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        List<Genre> genres = bookDto.getGenreNameList().stream()
                .map(genreName -> genreRepository.findByGenreName(genreName)
                        .orElseThrow(() -> new RuntimeException("Genre not found")))
                .collect(Collectors.toList());

        Book book = modelMapper.map(bookDto, Book.class);
        book.setAuthor(author);
        book.setGenres(genres);
        bookRepository.save(book);

        bookDto.getImgUrls().forEach(url -> {
            BookImage image = new BookImage();
            image.setImageUrl(url);
            image.setBook(book);
            imageRepository.save(image);
        });

        return bookDto;
    }

    public BooksResponse getBooksByName(String bookName, int pageNo, int pageSize) throws JsonProcessingException {
        BooksResponse booksResponse = bookRedisService.getSearchBook(bookName, pageNo, pageSize);
        if (booksResponse == null) {
            booksResponse = new BooksResponse();
            Page<Book> books = bookRepository.findAllByName(bookName.trim().toLowerCase(), PageRequest.of(pageNo, pageSize));
            List<Book> bookList = books.getContent();
            List<BookDto> bookDtos = bookList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            booksResponse.setBooks(bookDtos);
            booksResponse.setTotalPage(books.getTotalPages());
            booksResponse.setTotalCount(books.getTotalElements());
            bookRedisService.saveSearchBook(booksResponse, bookName, pageNo, pageSize);
        }
        return booksResponse;
    }

    private BookDto convertToDto(Book book) {
        BookDto bookDto = modelMapper.map(book, BookDto.class);
        if (book.getGenres() != null) {
            bookDto.setGenreNameList(
                    book.getGenres()
                            .stream()
                            .map(Genre::getGenreName)
                            .collect(Collectors.toList())
            );
        }
        if (book.getImages() != null) {
            bookDto.setImgUrls(
                    book.getImages()
                            .stream()
                            .map(BookImage::getImageUrl)
                            .collect(Collectors.toList())
            );
        }

        Inventory inventory = inventoryRepository.findByBookBookId(book.getBookId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        bookDto.setQuantityInStock(inventory.getStock());

        return bookDto;
    }

    public List<BookDto> getRandomBooks(int range) {
        List<Book> books = bookRepository.findRandomBooks(range);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBookByAuthor(String authorName) {
        List<Book> books = bookRepository.findByAuthorAuthorName(authorName);
        return books.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BooksResponse getBooksByGenre(String genreName, int pageNo, int pageSize) throws JsonProcessingException {
        BooksResponse booksResponse = bookRedisService.getSearchBook(genreName, pageNo, pageSize);
        if (booksResponse == null) {
            booksResponse = new BooksResponse();
            Genre genre = genreRepository.findByGenreName(genreName)
                    .orElseThrow(() -> new RuntimeException("Genre not found"));
            Page<Book> books = bookRepository.findByGenres(List.of(genre), PageRequest.of(pageNo, pageSize));
            List<BookDto> bookDtos = books.stream().map(this::convertToDto)
                    .collect(Collectors.toList());
            booksResponse.setBooks(bookDtos);
            booksResponse.setTotalPage(books.getTotalPages());
            booksResponse.setTotalCount(books.getTotalElements());
            bookRedisService.saveSearchBook(booksResponse, genreName, pageNo, pageSize);
        }

        return booksResponse;
    }
}
