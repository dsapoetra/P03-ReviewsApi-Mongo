package com.udacity.course3.reviews;

import com.udacity.course3.reviews.model.CommentDocument;
import com.udacity.course3.reviews.model.ReviewDocument;
import com.udacity.course3.reviews.repository.ReviewMongoRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReviewsMongoDbTest {

    @Autowired
    private ReviewMongoRepository reviewMongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSaveReview() {
        ReviewDocument review = new ReviewDocument("review content", new Date(), 99, 1);
        review.setId(1);

        mongoTemplate.save(review, "reviews");

        Assert.assertFalse(mongoTemplate.findAll(ReviewDocument.class, "reviews").isEmpty());
        Assert.assertEquals(1, Objects.requireNonNull(mongoTemplate.findById(1, ReviewDocument.class)).getId());
    }

    @Test
    public void testFindReviewById() {
        ReviewDocument review = new ReviewDocument("review content", new Date(), 5, 1);
        review.setId(1);

        reviewMongoRepository.save(review);

        Optional<ReviewDocument> opReview = reviewMongoRepository.findById(review.getId());

        Assert.assertEquals(review.getId(), opReview.get().getId());
    }

    @Test
    public void testFindReviewsByProductId() {
        ReviewDocument review1 = new ReviewDocument("review content", new Date(), 5, 1);
        review1.setId(0);
        ReviewDocument review2 = new ReviewDocument("review content", new Date(), 5, 1);
        review2.setId(1);

        ReviewDocument nReview1 = reviewMongoRepository.save(review1);
        ReviewDocument nReview2 = reviewMongoRepository.save(review2);

        List<ReviewDocument> reviews = reviewMongoRepository.findReviewsByProductId(1);

        Assert.assertEquals(2, reviews.size());
    }

    @Test
    public void testFindCommentsByReviewId() {
        ReviewDocument review = new ReviewDocument("review content", new Date(), 5, 1);
        review.setId(1);
        List<CommentDocument> comments = new ArrayList<CommentDocument>();
        comments.add(new CommentDocument("comment 1", new Date()));
        comments.add(new CommentDocument("comment 2", new Date()));
        review.setComments(comments);
        ReviewDocument nReview = reviewMongoRepository.save(review);

        Optional<ReviewDocument> opReview = reviewMongoRepository.findById(nReview.getId());

        Assert.assertEquals(2, opReview.get().getComments().size());
    }
}
