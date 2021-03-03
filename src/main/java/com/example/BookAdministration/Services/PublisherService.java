package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Repositories.PublisherRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {
    private final Logger logger = LoggerFactory.getLogger(PublisherService.class);
    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> getAllPublishers() {
        logger.info("Find all Publishers");

        return publisherRepository.findAll();
    }

    public Publisher getPublisherById(Long id) {
        logger.info("Find Publisher by id:" + id);

        return publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find Publisher with id:" + id));
    }

    public Publisher createPublisher(Publisher publisher) {
        logger.info("Create new Publisher");

        if (publisherRepository.findByNameAndCity(publisher.getName(), publisher.getCity()).isPresent()) {
            throw new EntityAlreadyExistException("This Publisher already exists");
        }

        return publisherRepository.save(publisher);
    }

    public HttpStatus deletePublisherById(Long id) {
        logger.info("Delete publisher with id:" + id);

        if (publisherRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Could not find Publisher with id:" + id);
        }

        publisherRepository.deleteById(id);

        return HttpStatus.OK;
    }

    public Publisher updatePublisher(Publisher publisher, Long id) {
        logger.info("Publisher with id:" + id + " will be updated");

        return publisherRepository.findById(id)
                .map(publisher1 -> {
                    if (publisherRepository.existsByName(publisher.getName())) {
                        throw new EntityAlreadyExistException("Publisher with this Name already Exists!");
                    }

                    publisher1.setCity(publisher.getCity());
                    publisher1.setName(publisher.getName());

                    return publisherRepository.save(publisher1);
                })
                .orElseGet(() -> {
                    publisher.setId(id);

                    return publisherRepository.save(publisher);
                });
    }
}
