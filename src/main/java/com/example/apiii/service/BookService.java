package com.example.apiii.service;

import com.example.apiii.Entity.Book;
import com.example.apiii.Entity.Member;
import com.example.apiii.repository.BookRepository;
import com.example.apiii.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findBooksByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    public boolean purchaseBook(Long bookId, String cardNumber) {
        Optional<Member> memberOpt = memberRepository.findById(cardNumber);
        if (!memberOpt.isPresent()) {
            throw new IllegalArgumentException("No member found with card number: " + cardNumber);
        }
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()) {
            throw new IllegalArgumentException("No book found with ID: " + bookId);
        }

        Member member = memberOpt.get();
        Book book = bookOpt.get();

        if (member.getBalance() >= book.getPrice()) {
            member.setBalance(member.getBalance() - book.getPrice());
            memberRepository.save(member);
            return true;
        }
        return false;
    }


}