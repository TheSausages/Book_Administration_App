package com.example.BookAdministration.Entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "invalid Author first name")
    private String firstName;

    @NotBlank(message = "Invalid Author last name")
    private String LastName;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] portrait;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    public long getId() {
        return id;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public byte[] getPortrait() {
        return portrait;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setPortrait(byte[] portrait) {
        this.portrait = portrait;
    }
}
