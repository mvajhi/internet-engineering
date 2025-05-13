package org.example.services;

import java.time.LocalDateTime;
import java.util.List;

import org.example.entities.Book;
import org.example.entities.BookShop;
import org.example.request.AddReviewRequest;
import org.example.entities.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    BookShop bookShop;

    public ReviewService(BookShop bookShop){
        this.bookShop = bookShop;
    }

    public ReviewService(){}

    public List<Review> paginatedSearch(String title, Integer page, Integer pageSize) {
        List<Review> reviews = bookShop.findReviews(title);
        if (page == null)
            page =0;
        if (pageSize == null)
            pageSize = 10;
        int start = page * pageSize;
        int end = Integer.min(reviews.size(), pageSize * (page +1));
        return reviews.subList(start, end);
    }

    public Review createReview(AddReviewRequest request){
        Review review = new Review(request.getUsername(), request.getTitle(),
                ratingIsValid(request.getRate()) ? request.getRate() : -1,
                request.getComment());
        review.setDate(LocalDateTime.now());
        return review;
    }

    public static float calculateAverageRating(Book book, List<Review> reviews){
        float number =0;
        float ratingSum =0;
        for (Review r : reviews) {
            if (r.getBookTitle().equals(book.getTitle())) {
                number++;
                ratingSum += r.getRate();
            }
        }
        if(number == 0)
            return 0;
        return ratingSum / number;
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
