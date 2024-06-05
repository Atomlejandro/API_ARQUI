package com.example.apiii.controller;

import com.example.apiii.Entity.Book;
import com.example.apiii.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.listAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        List<Book> books = bookService.findBooksByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<List<Book>> getBooksByIsbn(@PathVariable String isbn) {
        List<Book> books = bookService.findBooksByIsbn(isbn);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/price")
    public ResponseEntity<List<Book>> getBooksByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<Book> books = bookService.findBooksByPrice(minPrice, maxPrice);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable String genre) {
        List<Book> books = bookService.findBooksByGenre(genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/publishedDate")
    public ResponseEntity<List<Book>> getBooksByPublishedDate(@RequestParam Date startDate, @RequestParam Date endDate) {
        List<Book> books = bookService.findBooksByPublishedDate(startDate, endDate);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    @GetMapping("/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        List<Book> books = bookService.findBooksByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseBooks(@RequestParam String cardNumber, @RequestBody List<Long> bookIds) {
        try {
            boolean success = bookService.purchaseBooks(bookIds, cardNumber);
            if (success) {
                return new ResponseEntity<>("Purchase successful", HttpStatus.OK);
            }
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}