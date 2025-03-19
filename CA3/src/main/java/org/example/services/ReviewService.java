package org.example.services;

import java.time.LocalDateTime;
import java.util.List;

import org.example.entities.BookShop;
import org.example.request.AddReviewRequest;
import org.example.entities.Review;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    BookShop bookShop;

    public ReviewService(BookShop bookShop){
        this.bookShop = bookShop;
    }

    public ReviewService(){}

    public List<Review> paginatedSearch(String title, int page, int pageSize) {
        List<Review> reviews = bookShop.findReviews(title);
        return reviews.subList(page * pageSize, (page + 1) * (pageSize));
    }

    public Review createReview(AddReviewRequest request){
        Review review = new Review(request.getUsername(), request.getTitle(),
                ratingIsValid(request.getRate()) ? request.getRate() : -1,
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
