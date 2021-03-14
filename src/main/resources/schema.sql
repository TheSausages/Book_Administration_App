DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Publishers;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Authors;

CREATE TABLE Users (
    id IDENTITY NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(60) NOT NULL,
    matching_password VARCHAR(60) NOT NULL,

    CONSTRAINT NoBlackUsername CHECK (username != ''),
    CONSTRAINT NoBlackPassword CHECK (password != ''),
    CONSTRAINT NoBlackMatchingPassword CHECK (matching_password != '')
);

CREATE TABLE Publishers (
    id IDENTITY NOT NULL,
    name VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,

    CONSTRAINT NoBlackName CHECK (name != ''),
    CONSTRAINT NoBlackCity CHECK (city != '')
);

CREATE TABLE Authors (
    id IDENTITY NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth date,
    primary_genre ENUM('FANTASY', 'DRAMA', 'ScienceFiction', 'POETRY', 'Fiction', 'Mystery', 'Biography', 'Nonfiction'),
    portrait BLOB,

    CONSTRAINT NoBlackFirstName CHECK (first_name != ''),
    CONSTRAINT NoBlackLastName CHECK (last_name != '')
);

CREATE TABLE Books (
    id IDENTITY NOT NULL,
    title VARCHAR(100) NOT NULL,
    sub_title VARCHAR(100),
    description VARCHAR(1024),
    publishing_year SMALLINT NOT NULL DEFAULT 1400,
    cover BLOB,
    author_id BIGINT,
    publisher_id BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (author_id) REFERENCES Authors(id),
    FOREIGN KEY (publisher_id) REFERENCES Publishers(id),
    CONSTRAINT NoBlackTitle CHECK (title != '')
);