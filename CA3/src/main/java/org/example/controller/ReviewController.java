package org.example.controller;

import jdk.jfr.Description;
import org.example.entities.Review;
import org.example.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/search")
    @Description("search review base on book title")
    public List<Review> search(@RequestParam String title, @RequestParam int page, @RequestParam int pageSize){
        return reviewService.paginatedSearch(title, page, pageSize);
    }
}
