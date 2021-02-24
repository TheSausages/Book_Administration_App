package com.example.BookAdministration.Entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "Publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Invalid Publisher name")
    private String name;

    @NotBlank(message = "Invalid Publisher home city")
    private String city;

    @OneToMany(mappedBy = "publisher")
    private Set<Book> publishedBooks;

    public Publisher() {}

    public Publisher(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public Set<Book> getPublishedBooks() {
        return publishedBooks;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPublishedBooks(Set<Book> publishedBooks) {
        this.publishedBooks = publishedBooks;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }
}
