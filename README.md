# Book Administration App
Web Application for managing a book Collection. User can perforn multiple operations(add, delete, change) on 3 objects: Books, Authors and Publishers. 
Uses a simple web interface made using thymeleaf with a CRUD backend. The app is secured using Spring Security.

Comes with preloded data:
- A User:
- - Username: user
- - Password: pass
- 5 Book
- 3 Authors
- 4 Publishers

## Table of Content
- [Features](#Features)
- [Screenshot](#Screenshots)
- [Technologies](#Technologies)
- [Author](#Author)

## Features
- Login and User Registration
- Creating:
- - Books (contains:
- - - Title, Subtitle and Description
- - - Publishing Year
- - - Cover
- - - Publishers and Authors from the Database)
- - Authors (contains:
- - - First and LastName
- - - Date of birth
- - - Primary Genre from List
- - - Portrait)
- - Publishers (contains:
- - - Name
- - - City)
- Viewing, editing and deleting:
- - Books
- - Authors
- - Publishers
- Validating if:
- - If Users, Books, Authors or Publishers already exist
- - Input fields are not empty
- - Username and Password are correct

## Screenshots

![Login Page](/src/main/resources/static/img/GithubScreens/Login.png)
![Registration Page](/src/main/resources/static/img/GithubScreens/Registration.png)
![Book Catalog](/src/main/resources/static/img/GithubScreens/Books.png)
![Author List](/src/main/resources/static/img/GithubScreens/Authors.png)
![Publisher List](/src/main/resources/static/img/GithubScreens/Publishers.png)
![Add Book 1](/src/main/resources/static/img/GithubScreens/AddBook1.png)
![Add Book 2](/src/main/resources/static/img/GithubScreens/AddBook2.png)
![Logout Page](/src/main/resources/static/img/GithubScreens/LogOut.png)

## Technologies used:
- Java 11
- Spring boot 2.4.3
- Spring 
- - Data JPA
- - Security
- - Thymeleaf
- - Validation
- - Web
- - Hibarnate
- H2 database
- JUnit 5
- Mockito
- Gradle
- HTML
- CSS

# Author
Kacper Ziejło
