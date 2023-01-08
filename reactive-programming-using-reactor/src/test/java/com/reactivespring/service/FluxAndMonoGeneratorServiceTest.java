package com.reactivespring.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        final var namesFlux = service.namesFlux();

        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        final var namesFlux = service.namesFluxMap();

        StepVerifier.create(namesFlux)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        final var namesFlux = service.namesFluxImmutability();

        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxFilter() {
        final var namesFlux = service.namesFluxFilter(3);

        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        final var namesFlux = service.namesFluxFlatMap(3);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformDefaultIfEmpty() {
        final var namesFlux = service.namesFluxTransformDefaultIfEmpty(3);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformSwitchIfEmpty() {
        final var namesFlux = service.namesFluxTransformSwitchIfEmpty(6);

        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreConcat() {
        final var namesFlux = service.exploreConcat();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreConcatWith() {
        final var namesFlux = service.exploreConcatWith();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreConcatWithMono() {
        final var namesFlux = service.exploreConcatWithMono();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreMerge() {
        final var namesFlux = service.exploreMerge();

        StepVerifier.create(namesFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreMergeWith() {
        final var namesFlux = service.exploreMergeWith();

        StepVerifier.create(namesFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }


    @Test
    void namesFluxExploreMergeWithMono() {
        final var namesFlux = service.exploreMergeWithMono();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreMergeSequential() {
        final var namesFlux = service.exploreMergeSequential();

        StepVerifier.create(namesFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreZip() {
        final var namesFlux = service.exploreZip();

        StepVerifier.create(namesFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreZip_1() {
        final var namesFlux = service.exploreZip1();

        StepVerifier.create(namesFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreZipWith() {
        final var namesFlux = service.exploreZipWith();

        StepVerifier.create(namesFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void namesFluxExploreZipWithMono() {
        final var namesFlux = service.exploreZipWithMono();

        StepVerifier.create(namesFlux)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        final var namesFlux = service.namesFluxFlatMapAsync(3);

        StepVerifier.create(namesFlux)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFluxConcatMap() {
        final var namesFlux = service.namesFluxConcatMap(3);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMap() {
        final var namesMono = service.nameMonoFlatMap(3);

        StepVerifier.create(namesMono)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMapMany() {
        final var namesMono = service.nameMonoFlatMapMany(3);

        StepVerifier.create(namesMono)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }
}