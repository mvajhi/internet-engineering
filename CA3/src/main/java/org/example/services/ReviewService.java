package org.example.services;

import java.time.LocalDateTime;

import org.example.request.AddReviewRequest;
import org.example.entities.Review;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    public Review createReview(AddReviewRequest request){
        Review review = new Review(request.getUsername(), request.getTitle(),
                ratingIsValid(request.getRating()) ? request.getRating() : -1,
                request.getComment());
        review.setDate(LocalDateTime.now());
        return review;
    }
    private boolean ratingIsValid(int rating){
        if (rating < 1 || rating > 5){
            return false;
        }
        else{
            return true;
        }
    }
}
