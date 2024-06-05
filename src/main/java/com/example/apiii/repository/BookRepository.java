package com.example.apiii.repository;

import com.example.apiii.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
    List<Book> findByIsbn(String isbn);
    List<Book> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Book> findByGenre(String genre);
    List<Book> findByAuthor(String author);
    List<Book> findByPublishedDateBetween(Date startDate, Date endDate);
}
