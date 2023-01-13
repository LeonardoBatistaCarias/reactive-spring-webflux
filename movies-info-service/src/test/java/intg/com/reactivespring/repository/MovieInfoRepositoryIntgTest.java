package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository repository;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
        repository.saveAll(movieInfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void findAll() {
        final var moviesInfoFlux = repository.findAll().log();
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        final var moviesInfoMono = repository.findById("abc").log();
        StepVerifier.create(moviesInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        final var moviesInfoMono = repository.save(new MovieInfo(null, "Batman Eternal",
                2005, List.of("Christian Bale", "John Castle"), LocalDate.parse("2012-07-20"))).log();
        StepVerifier.create(moviesInfoMono)
                .assertNext(movieInfo -> {
                    assertNotNull(movieInfo.getMovieInfoId());
                    assertEquals("Batman Eternal", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        final var actualMovieInfo = repository.findById("abc").block();
        actualMovieInfo.setYear(2023);

        final var movieInfoMono = repository.save(actualMovieInfo).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertNotNull(movieInfo.getMovieInfoId());
                    assertEquals(2023, movieInfo.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        repository.deleteById("abc").block();
        final var moviesInfoFlux = repository.findAll().log();

        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findMovieInfoByYear() {
        var movieInfosFlux = repository.findByYear(2005).log();

        StepVerifier.create(movieInfosFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByName() {
        var movieInfosMono = repository.findByName("Batman Begins").log();

        StepVerifier.create(movieInfosMono)
                .expectNextCount(1)
                .verifyComplete();
    }

}