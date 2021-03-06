package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.model.Product;
import com.udacity.course3.reviews.model.Review;
import com.udacity.course3.reviews.model.ReviewDocument;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    // TODO: Wire JPA repositories here
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewMongoRepository reviewMongoRepository;

    /**
     * Creates a review for a product.
     * <p>
     * 1. Add argument for review entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of product.
     * 3. If product not found, return NOT_FOUND.
     * 4. If found, save review.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<ReviewDocument> createReviewForProduct(@PathVariable("productId") Integer productId,
                                                         @RequestBody Map<String, String> review) {
        Optional<Product> opProduct = productRepository.findById(productId);

        if (!opProduct.isPresent()) {
            return new ResponseEntity<ReviewDocument>(HttpStatus.NOT_FOUND);
        }

        String content = review.get("content");
        int rating = Integer.parseInt(review.get("rating"));
        Date date = new Date();
        Product product = opProduct.get();

        Review nReview = new Review(content, date, rating, product);
        reviewRepository.save(nReview);

        // Persist to Mongodb
        ReviewDocument nReviewDocument = new ReviewDocument(content, date, rating, productId);
        nReviewDocument.setId(nReview.getReviewId());
        reviewMongoRepository.save(nReviewDocument);

        return new ResponseEntity<ReviewDocument>(nReviewDocument, HttpStatus.OK);
    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReviewDocument>> listReviewsForProduct(@PathVariable("productId") Integer productId) {
        Optional<Product> opProduct = productRepository.findById(productId);

        if(!opProduct.isPresent()) {
            return new ResponseEntity<List<ReviewDocument>>(HttpStatus.NOT_FOUND);
        }

        List<Review> reviews = reviewRepository.findReviewsByProduct(opProduct.get());

        if(reviews.isEmpty()) {
            return new ResponseEntity<List<ReviewDocument>>(HttpStatus.NOT_FOUND);
        }

        List<ReviewDocument> mReviews = new ArrayList<ReviewDocument>();
        reviews.forEach(review -> {
            Optional<ReviewDocument> opReview = reviewMongoRepository.findById(review.getReviewId());
            if(opReview.isPresent()) {
                mReviews.add(opReview.get());
            } else {
                System.out.println("Error: Review exists in SQL but not in MongoDB.");
            }
        });

        return new ResponseEntity<List<ReviewDocument>>(mReviews, HttpStatus.OK);
    }
}