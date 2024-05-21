package com.example.apiii.controller;

import com.example.apiii.Entity.Book;
import com.example.apiii.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/purchase/{bookId}")
    public ResponseEntity<String> purchaseBook(@PathVariable Long bookId, @RequestParam String cardNumber) {
        try {
            boolean success = bookService.purchaseBook(bookId, cardNumber);
            if (success) {
                return new ResponseEntity<>("Purchase successful", HttpStatus.OK);
            }
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}