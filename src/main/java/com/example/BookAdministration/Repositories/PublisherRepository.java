package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Publisher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    boolean existsByNameAndCity(String name, String city);

    Optional<Publisher> findByName(String name);
}
