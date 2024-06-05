package com.example.apiii.service;

import com.example.apiii.Entity.Book;
import com.example.apiii.Entity.Member;
import com.example.apiii.Entity.TransactionLog;
import com.example.apiii.repository.BookRepository;
import com.example.apiii.repository.MemberRepository;
import com.example.apiii.repository.TransactionLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TransactionLogRepository transactionLogRepository;
    @Autowired
    private HttpServletRequest request;

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findBooksByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    public List<Book> findBooksByPrice(Double minPrice, Double maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> findBooksByPublishedDate(Date startDate, Date endDate) {
        return bookRepository.findByPublishedDateBetween(startDate, endDate);
    }

    public boolean purchaseBooks(List<Long> bookIds, String cardNumber) {
        Optional<Member> memberOpt = memberRepository.findById(cardNumber);
        if (!memberOpt.isPresent()) {
            throw new IllegalArgumentException("No member found with card number: " + cardNumber);
        }

        Member member = memberOpt.get();
        double totalCost = 0;
        Map<Long, Integer> bookCountMap = new HashMap<>();

        // Contar la cantidad de cada libro en la solicitud
        for (Long bookId : bookIds) {
            bookCountMap.put(bookId, bookCountMap.getOrDefault(bookId, 0) + 1);
        }

        // Verificar la cantidad de cada libro
        for (Map.Entry<Long, Integer> entry : bookCountMap.entrySet()) {
            Long bookId = entry.getKey();
            int requestedQuantity = entry.getValue();

            Optional<Book> bookOpt = bookRepository.findById(bookId);
            if (!bookOpt.isPresent()) {
                throw new IllegalArgumentException("No book found with ID: " + bookId);
            }

            Book book = bookOpt.get();
            if (book.getStock() < requestedQuantity) {
                throw new IllegalArgumentException("Not enough stock for book with ID: " + bookId + ". Requested: " + requestedQuantity + ", Available: " + book.getStock());
            }

            totalCost += book.getPrice() * requestedQuantity;
        }

        if (member.getBalance() < totalCost) {
            return false;
        }


        for (Map.Entry<Long, Integer> entry : bookCountMap.entrySet()) {
            Long bookId = entry.getKey();
            int requestedQuantity = entry.getValue();

            Optional<Book> bookOpt = bookRepository.findById(bookId);
            Book book = bookOpt.get();
            book.setStock(book.getStock() - requestedQuantity);
            bookRepository.save(book);
        }

        member.setBalance(member.getBalance() - totalCost);
        memberRepository.save(member);


        // Registrar el log de transacción
        String ipAddress = request.getRemoteAddr();
        TransactionLog log = new TransactionLog();
        log.setId(UUID.randomUUID().toString());
        log.setCardNumber(cardNumber);
        log.setTotalAmount(totalCost);
        log.setBookIds(bookIds);
        log.setTransactionDate(new Date());
        log.setIpAddress(ipAddress);

        transactionLogRepository.save(log);

        return true;
    }


}