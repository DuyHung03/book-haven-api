package com.duyhung.bookstoreapi.service;

import com.duyhung.bookstoreapi.dto.BookDto;
import com.duyhung.bookstoreapi.entity.*;
import com.duyhung.bookstoreapi.repository.*;
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

        for (String url : bookDto.getImgUrls()) {
            BookImage image = new BookImage();
            image.setImageUrl(url);
            image.setBook(book);
            imageRepository.save(image);
        }

        return bookDto;
    }

    public List<BookDto> getBooksByName(String bookName, int pageNo, int pageSize) {
        Page<Book> books = bookRepository.findAllByName(bookName, PageRequest.of(pageNo,pageSize));

        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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

    public List<BookDto> getBooksByGenre(String genreName) {
        Genre genres = genreRepository.findByGenreName(genreName).orElseThrow(
                () -> new RuntimeException("Genre not found")
        );
        List<Book> books = bookRepository.findByGenres(List.of(genres));
        return books.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

}
