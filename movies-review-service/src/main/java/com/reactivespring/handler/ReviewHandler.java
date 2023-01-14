package com.reactivespring.handler;

import com.mongodb.internal.connection.Server;
import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

    private ReviewReactiveRepository repository;

    public ReviewHandler(ReviewReactiveRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(Review.class)
                .flatMap(repository::save)
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        final var movieInfoId = request.queryParam("movieInfoId");

        final Flux<Review> reviewsFlux;
        if(movieInfoId.isPresent()) {
            reviewsFlux = repository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
        } else {
            reviewsFlux = repository.findAll();
        }
        return ServerResponse.ok().body(reviewsFlux, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        final var reviewId = request.pathVariable("id");
        final var existingReview = repository.findById(reviewId);

        return existingReview
                .flatMap(review -> request.bodyToMono(Review.class)
                .map(reqReview -> {
                    review.setComment(reqReview.getComment());
                    review.setRating(reqReview.getRating());
                    return review;
                })
                .flatMap(repository::save))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        final var reviewId = request.pathVariable("id");
        final var existingReview = repository.findById(reviewId);

        return existingReview
                .flatMap(review -> repository.deleteById(reviewId))
                .then(ServerResponse.noContent().build());
    }
}
