package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Publisher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByNameAndCity(String name, String city);

    boolean existsByName(String name);
}
