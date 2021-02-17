package com.example.BookAdministration.Repositories;

import com.example.BookAdministration.Entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Long, Publisher> {
}
