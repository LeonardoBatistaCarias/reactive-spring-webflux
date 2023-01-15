package com.reactivespring.client;

import com.reactivespring.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Component
public class ReviewRestClient {

    @Value("${restClient.reviewsUrl}")
    private String reviewsUrl;
    private WebClient webClient;

    public ReviewRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Review> retrieveReviews(String movieId) {
        final var url = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand().toUriString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class);
    }


}
