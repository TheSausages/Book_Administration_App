package com.example.BookAdministration;

import com.example.BookAdministration.Entities.Author;
import com.example.BookAdministration.Entities.Book;
import com.example.BookAdministration.Entities.PrimaryGenre;
import com.example.BookAdministration.Entities.Publisher;
import com.example.BookAdministration.Repositories.AuthorRepository;
import com.example.BookAdministration.Repositories.BookRepository;
import com.example.BookAdministration.Repositories.PublisherRepository;
import com.example.BookAdministration.Services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.time.LocalDate;

@Configuration
public class LoadDatabase {

    @Bean
    public CommandLineRunner initDatabase(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository, BookService bookService) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher1", "Rzeszow")));
            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher2", "Krasne")));
            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher3", "Wrocław")));
            logger.info("Preloading:" + publisherRepository.save(new Publisher("Publisher4", "Warszawa")));

            InputStream au1 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/JRRTolkien.jpg"));
            InputStream au2 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/TappeiNagatsuki.jpg"));
            InputStream au3 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/WojciechDrewniak.jpg"));

            logger.info("Preloading:" + authorRepository.save(new Author("John Ronald Reuel", "Tolkien", LocalDate.of(1892,1,3), au1.readAllBytes(), PrimaryGenre.Fantasy)));
            logger.info("Preloading:" + authorRepository.save(new Author("Tappei", "Nagatsuki", LocalDate.of(1987,3,11), au2.readAllBytes(), PrimaryGenre.Fantasy)));
            logger.info("Preloading:" + authorRepository.save(new Author("Wojciech", "Drewniak", LocalDate.of(1977,10,26), au3.readAllBytes(), PrimaryGenre.Nonfiction)));

            InputStream ks1 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/TheFellowshipOfTheRing.jpg"));
            InputStream ks2 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/TheTwoTowers.jpg"));
            InputStream ks3 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/theReturnOfTheKing.jpg"));
            InputStream ks4 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/ReZeroVol1.jpg"));
            InputStream ks5 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/HistoriaBezCenzury3.jpeg"));
            InputStream ks6 = (Thread.currentThread().getContextClassLoader().getResourceAsStream("static/img/HistoriaBezCenzury1.jpg"));

            Book book1 = new Book("The Fellowship of the Ring", "The Lord of the Rings Part One",
                    "The Lord of the Rings.Sauron, the Dark Lord, has gathered to him all the Rings of Power the means by which he intends to rule Middle-earth. All he lacks in his plans for dominion is the One Ring the ring that rules them all which has fallen into the hands of the hobbit, Bilbo Baggins.In a sleepy village in the Shire, young Frodo Baggins finds himself faced with an immense task, as his elderly cousin Bilbo entrusts the Ring to his care. Frodo must leave his home and make a perilous journey across Middle-earth to the Cracks of Doom, there to destroy the Ring and foil the Dark Lord in his evil purpose.",
                    1954, ks1.readAllBytes(), authorRepository.findById(1l).get(), publisherRepository.findById(1l).get());
            logger.info("Preloading:" + bookRepository.save(book1));

            Book book2 = new Book("The Two Towers", "The Lord of the Rings Part Two",
                    " Frodo and the Companions of the Ring have been beset by danger during their quest to prevent the Ruling Ring from falling into the hands of the Dark Lord by destroying it in the Cracks of Doom. They have lost the wizard, Gandalf, in the battle with an evil spirit in the Mines of Moria; and at the Falls of Rauros, Boromir, seduced by the power of the Ring, tried to seize it by force. While Frodo and Sam made their escape the rest of the company were attacked by Orcs. Now they continue their journey alone down the great River Anduin -- alone, that is, save for the mysterious creeping figure that follows wherever they go.",
                    1954, ks2.readAllBytes(), authorRepository.findById(1l).get(), publisherRepository.findById(1l).get());
            logger.info("Preloading:" + bookRepository.save(book2));

            Book book3 = new Book("The Return of the King", "The Lord of the Rings Part Three",
                    "While the evil might of the Dark Lord Sauron swarms out to conquer all Middle-earth, Frodo and Sam struggle deep into Mordor, seat of Sauron’s power. To defeat the Dark Lord, the One Ring, ruler of all the accursed Rings of Power, must be destroyed in the fires of Mount Doom. But the way is impossibly hard, and Frodo is weakening. Weighed down by the compulsion of the Ring, he begins finally to despair.",
                    1955, ks3.readAllBytes(), authorRepository.findById(1l).get(), publisherRepository.findById(1l).get());
            logger.info("Preloading:" + bookRepository.save(book3));

            Book book4 = new Book("Re:zero Starting Life In Another World Vol. 1", "",
                    "Subaru Natsuki was just trying to get to the convenience store but wound up summoned to another world. He encounters the usual things--life-threatening situations, silver haired beauties, cat fairies--you know, normal stuff. All that would be bad enough, but he's also gained the most inconvenient magical ability of all--time travel, but he's got to die to use it. How do you repay someone who saved your life when all you can do is die?",
                    2014, ks4.readAllBytes(), authorRepository.findById(2l).get(), publisherRepository.findById(2l).get());
            logger.info("Preloading:" + bookRepository.save(book4));

            Book book5 = new Book("Historia Bez Cenzury", "",
                    "Ostrzeżenie!\n" +
                            "Książka zawiera treści nieodpowiednie dla dzieci oraz kontrowersyjne skróty myślowe. Jeśli jest to dla Ciebie poważny problem, zrezygnuj z czytania… chociaż pewnie będziesz żałować.\n" +
                            "\n" +
                            "\"Po co ta książka, skoro wszystko jest w internetowym programie? Tak się składa, że nie wszystko. Nie da się w piętnastominutowym odcinku opowiedzieć o jakimś władcy od początku do końca. Wiele ciekawych i ważnych rzeczy musimy sobie darować. W tej książce dostajecie »Historię Bez Cenzury« w wersji full wypas. Nie było żadnych ograniczeń długości tekstu. Mogłem pisać do momentu, aż poczułem: Ok, Wojtas, to chyba wszystko.\"\n" +
                            "Wojtek Drewniak",
                    2016, ks5.readAllBytes(), authorRepository.findById(3l).get(), publisherRepository.findById(3l).get());
            logger.info("Preloading:" + bookRepository.save(book5));

            Book book6 = new Book("Historia bez cenzury 3", "Poland first to fight... czyli II wojna swiatowa",
                    "Do trzech razy sztuka!\n" +
                            "\n" +
                            "Byli polscy władcy, były wielkie koksy… Nadszedł czas na trzecią część serii! Oto przed wami II Wojna Światowa bez cenzury!\n" +
                            "\n" +
                            "Załóż mundur, zgarnij karabin pod pachę i wyrusz w podróż po najsłynniejszych frontach II wojny światowej. Poznaj tajemnice, o których nie dowiesz się z lekcji w szkole. Odkryj kulisy rozszyfrowania enigmy, unieś się w przestworza podczas bitwy o Anglię i dowiedz się, czemu zawsze to my Polacy byliśmy \"First to fight\" chociaż nikt się z nami nie cackał.",
                    2018, ks6.readAllBytes(), authorRepository.findById(3l).get(), publisherRepository.findById(4l).get());
            logger.info("Preloading:" + bookRepository.save(book6));
        };
    }
}
