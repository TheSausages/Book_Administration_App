package com.example.BookAdministration.Services;

import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Exceptions.EntityAlreadyExistException;
import com.example.BookAdministration.Exceptions.EntityNotFoundException;
import com.example.BookAdministration.Repositories.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    void getAllPublishers_NoErrors_NormalBehavior() {
        //given
        Publisher publisher = new Publisher();
        when(publisherRepository.findAll()).thenReturn(Arrays.asList(publisher));

        //when
        List<Publisher> result = publisherService.getAllPublishers();

        //then
        assertEquals(result, Arrays.asList(publisher));
    }

    @Test
    void getPublisherById_NoSuchPublisher_ThrowException() {
        //given
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            publisherService.getPublisherById(1L);
        });

        //then
        assertEquals("Could not find Publisher with id:1", exception.getMessage());
    }

    @Test
    void getPublisherById_NoErrors_NormalBehavior() {
        //given
        Publisher publisher = new Publisher();
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

        //when
        Publisher publisherReturned = publisherService.getPublisherById(1L);

        //then
        assertEquals(publisherReturned, publisher);
    }

    @Test
    void createPublisher_PublisherAlreadyExists_ThrowException() {
        //given
        Publisher publisher = setTypicalParams(new Publisher());
        when(publisherRepository.existsByNameAndCity("Publisher", "City")).thenReturn(true);

        //when
        RuntimeException exception = assertThrows(EntityAlreadyExistException.class, () -> {
            publisherService.createPublisher(publisher);
        });

        //then
        assertEquals("This Publisher already exists", exception.getMessage());
    }

    @Test
    void createPublisher_NoErrors_NormalBehavior() {
        //given
        Publisher publisher = setTypicalParams(new Publisher());
        when(publisherRepository.existsByNameAndCity("Publisher", "City")).thenReturn(false);
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        //when
        Publisher publisherReturned = publisherService.createPublisher(publisher);

        //then
        assertEquals(publisherReturned, publisher);
    }

    @Test
    void deletePublisherById_NoSuchPublisher_ThrowError() {
        //given
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
            publisherService.getPublisherById(1L);
        });

        //then
        assertEquals("Could not find Publisher with id:1", exception.getMessage());
    }

    @Test
    void deletePublisherById_NoErrors_normalBehavior() {
        //given
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(new Publisher()));

        //when
        HttpStatus status = publisherService.deletePublisherById(1L);

        //then
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void updatePublisher_PublisherAlreadyExists_ThrowException() {
        //given
        Publisher publisher = setTypicalParams(new Publisher());
        publisher.setId(2L);
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(publisherRepository.findByName("Publisher")).thenReturn(Optional.of(new Publisher()));

        //when
        RuntimeException exception = assertThrows(EntityAlreadyExistException.class, () -> {
            publisherService.updatePublisher(publisher, 1L);
        });

        //then
        assertEquals("Publisher with this Name already Exists!", exception.getMessage());
    }

    @Test
    void updatePublisher_PublisherAlreadyExists_UpdateInfo() {
        //given
        Publisher publisher = setTypicalParams(new Publisher());
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(publisherRepository.findByName("Publisher")).thenReturn(Optional.of(setTypicalParams(new Publisher())));
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        //when
        Publisher publisherReturned = publisherService.updatePublisher(publisher, 1L);

        //then
        assertEquals(publisherReturned, publisher);
    }

    @Test
    void updatePublisher_PublisherDoesNotExists_CreatePublisher() {
        //given
        Publisher publisher = setTypicalParams(new Publisher());
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        //when
        Publisher publisherReturned = publisherService.updatePublisher(publisher, 1L);

        //then
        assertEquals(publisherReturned, publisher);
    }

    private static Publisher setTypicalParams(Publisher publisher) {
        publisher.setId(1L);
        publisher.setName("Publisher");
        publisher.setCity("City");

        return publisher;
    }
}