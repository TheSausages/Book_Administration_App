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
    private String City;

    @OneToMany(mappedBy = "publisher")
    private Set<Book> publishedBooks;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Book> getPublishedBooks() {
        return publishedBooks;
    }

    public String getCity() {
        return City;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setPublishedBooks(Set<Book> publishedBooks) {
        this.publishedBooks = publishedBooks;
    }
}
