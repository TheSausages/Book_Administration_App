package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByNameAndCity(String name, String city);

    //@Query(value = "select case when count(pub) > 0 then true else false end from Entity pub where name=?1")
    boolean existsByName(String name);
}
