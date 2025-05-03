package org.example.response;

import org.example.entities.Review;

import java.util.List;

public class ShowReviewResponse {
    List<Review> reviews;
    float averageRating;

    public ShowReviewResponse(List<Review> reviews, float averageRating) {
        this.reviews=reviews;
        this.averageRating=averageRating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews=reviews;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating=averageRating;
    }
}
