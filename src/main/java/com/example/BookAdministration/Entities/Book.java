package com.example.BookAdministration.Entities;


import org.hibernate.validator.constraints.Range;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "A book must have a title!")
    private String title;

    private String description;

    @Range(min = 1400, max = 2100, message = "Invalid publishing year - outside of range <1400, 2100>")
    @NotNull(message = "Invalid publishing year")
    private Integer publishingYear;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] cover;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Publisher publisher;

    public Book() {}

    public Book(String title, String description, Integer publishingYear, byte[] cover, Author author, Publisher publisher) {
        this.title = title;
        this.description = description;
        this.publishingYear = publishingYear;
        this.author = author;
        this.publisher = publisher;
        this.cover = cover;
    }

    public Book(String title, String description, Integer publishingYear, Author author, Publisher publisher) {
        this.title = title;
        this.description = description;
        this.publishingYear = publishingYear;
        this.author = author;
        this.publisher = publisher;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Author getAuthor() {
        return author;
    }

    public byte[] getCover() {
        return cover;
    }

    public Integer getPublishingYear() {
        return publishingYear;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public void setPublishingYear(Integer publishingYear) {
        this.publishingYear = publishingYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParamsFromAnotherBook(Book book) {
        this.title = book.getTitle();
        this.publishingYear = book.getPublishingYear();
        this.description = book.getDescription();
        this.cover = book.getCover();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
    }
}
