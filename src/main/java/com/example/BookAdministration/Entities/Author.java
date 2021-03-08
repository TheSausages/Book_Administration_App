package com.example.BookAdministration.Entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "Authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "invalid Author first name")
    private String firstName;

    @NotBlank(message = "Invalid Author last name")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private PrimaryGenre primaryGenre;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] portrait;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    public Author() {};

    public Author(String firstName, String lastName, LocalDate dateOfBirth, byte[] portrait, PrimaryGenre primaryGenre) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.portrait = portrait;
        this.dateOfBirth = dateOfBirth;
        this.primaryGenre = primaryGenre;
    }

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
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public PrimaryGenre getPrimaryGenre() {
        return primaryGenre;
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
        this.lastName = lastName;
    }

    public void setPortrait(byte[] portrait) {
        this.portrait = portrait;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPrimaryGenre(PrimaryGenre primaryGenre) {
        this.primaryGenre = primaryGenre;
    }

    public void setParamsFromAnotherAuthor(Author author) {
        this.firstName = author.getFirstName();
        this.lastName = author.getLastName();
        this.primaryGenre = author.getPrimaryGenre();
        this.dateOfBirth = author.getDateOfBirth();

        if (author.getPortrait() != null) {
            this.portrait = author.getPortrait();
        }
    }
}
