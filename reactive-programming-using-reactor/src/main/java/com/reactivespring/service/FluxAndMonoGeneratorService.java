package com.reactivespring.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")) // db or a remote service call
                .log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")) // db or a remote service call
                .map(String::toUpperCase)
                .log();
    }

    public Flux<String> namesFluxImmutability() {
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe")); // db or a remote service call
        namesFlux.map(String::toUpperCase);

        return namesFlux;
    }

    public Flux<String> namesFluxFilter(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                        .map(String::toUpperCase)
                        .filter(s -> s.length() > stringLength)
                        .map(s -> s.length() + "-" + s);
    }

    public Flux<String> namesFluxFlatMap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s))
                .log();
    }

    public Flux<String> namesFluxConcatMap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(s -> splitStringWithDelay(s))
                .log();
    }

    private Flux<String> splitString(String name) {
        final var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> namesFluxFlatMapAsync(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitStringWithDelay(s))
                .log();
    }

    public Flux<String> namesFluxTransformDefaultIfEmpty(int stringLength) {
        Function <Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> exploreConcat() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> exploreConcatWith() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> exploreConcatWithMono() {
        final var aMono = Mono.just("A");
        final var bMono = Mono.just("B");

        return aMono.concatWith(bMono).log();
    }

    public Flux<String> exploreMerge() {
        final var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        final var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> exploreMergeWith() {
        final var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        final var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> exploreMergeWithMono() {
        final var aMono = Mono.just("A");
        final var bMono = Mono.just("B");

        return aMono.mergeWith(bMono).log();
    }

    public Flux<String> exploreMergeSequential() {
        final var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        final var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    public Flux<String> exploreZip() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second).log();
    }

    public Flux<String> exploreZip1() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        final var _123Flux = Flux.just("1", "2", "3");
        final var _456Flux = Flux.just("4", "5", "6");


        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log();
    }

    public Flux<String> exploreZipWith() {
        final var abcFlux = Flux.just("A", "B", "C");
        final var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second).log();
    }

    public Mono<String> exploreZipWithMono() {
        final var aMono = Mono.just("A");
        final var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log();
    }

    public Flux<String> namesFluxTransformSwitchIfEmpty(int stringLength) {
        Function <Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s));

        final var defaultFlux = Flux.just("default")
                .transform(filterMap);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    private Flux<String> splitStringWithDelay(String name) {
        final var charArray = name.split("");
        final var delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }
    public Mono<String> nameMono() {
        return Mono.just("alex")
                .log();
    }

    public Mono<String> nameMonoFilter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
    }

    public Mono<List<String>> nameMonoFlatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> nameMonoFlatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        final var charArray = s.split("");
        final var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public static void main(String[] args) {
        final var service = new FluxAndMonoGeneratorService();

        service.namesFlux().subscribe(name -> System.out.println("Name is : " + name));

        service.nameMono().subscribe(name -> System.out.println("Mono name is : " + name));
    }
}
