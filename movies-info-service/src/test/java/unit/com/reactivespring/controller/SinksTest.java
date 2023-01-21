package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.stream.IntStream;

public class SinksTest {

    @Test
    void sink() {
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux = replaySink.asFlux();
        integerFlux.subscribe((i) -> {
            System.out.println("Subscriber 1 : " + i);
        });

        Flux<Integer> integerFlux2 = replaySink.asFlux();
        integerFlux2.subscribe((i) -> {
            System.out.println("Subscriber 2 : " + i);
        });

        replaySink.tryEmitNext(3);

        Flux<Integer> integerFlux3 = replaySink.asFlux();
        integerFlux3.subscribe((i) -> {
            System.out.println("Subscriber 3 : " + i);
        });
    }

    @Test
    void sink_multicast() throws InterruptedException {

        Sinks.Many<Integer> multiCast = Sinks.many().multicast().onBackpressureBuffer();

        IntStream.rangeClosed(0,300)
                .forEach(multiCast::tryEmitNext);


        multiCast.tryEmitNext(301);
        multiCast.tryEmitNext(302);

        Flux<Integer> integerFlux = multiCast.asFlux();
        integerFlux
                .subscribe(s->{
                    System.out.println("Subscriber 1 : " + s);
                });

        multiCast.tryEmitNext(303);

        Flux<Integer> integerFlux1 = multiCast.asFlux();

        integerFlux1
                .subscribe(s->{
                    System.out.println("Subscriber 2 : " + s);
                });

        multiCast.tryEmitNext(4);
    }
}
