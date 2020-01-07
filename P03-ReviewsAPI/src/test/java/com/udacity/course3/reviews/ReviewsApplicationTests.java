package com.udacity.course3.reviews;

import com.udacity.course3.reviews.model.*;
import com.udacity.course3.reviews.repository.CommentRepository;
import com.udacity.course3.reviews.repository.ProductRepository;
import com.udacity.course3.reviews.repository.ReviewRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReviewsApplicationTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Before
    public void setUp() {
    }

    @Test
    public void contextLoads() {
        assertNotNull(productRepository);
        assertNotNull(reviewRepository);
        assertNotNull(commentRepository);
    }

    @Test
    public void testFindProductById() {
        Product product = new Product("name", "desc", 1F);

        productRepository.save(product);

        Product actual = productRepository.findById(product.getProductId()).get();

        assertNotNull(actual);
        assertEquals(product.getProductId(), actual.getProductId());
        assertEquals(product.getDescription(), actual.getDescription());
        assertEquals(product.getPrice(), actual.getPrice(), 0F);
    }

    @Test
    public void testFindReviewsByProduct() throws ParseException {
        Product product = new Product("name", "desc", 1F);
        productRepository.save(product);

        Date date = new SimpleDateFormat("yyyy/MM/dd").parse("2019/01/01");
        Review review = new Review("content", date, 99, product);

        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findReviewsByProduct(product);
        Review actualReview = reviews.get(0);

        assertEquals(1, reviews.size());
        assertEquals(review.getContent(), actualReview.getContent());
        assertEquals(review.getComments(), actualReview.getComments());
        assertEquals(review.getCreatedDate(), actualReview.getCreatedDate());
        assertEquals(review.getProduct(), actualReview.getProduct());
        assertEquals(review.getRating(), actualReview.getRating());
        assertEquals(review.getReviewId(), actualReview.getReviewId());

    }

	@Test
	public void testFindCommentsByReview() throws ParseException {
        Product product = new Product("name", "desc", 1F);
        productRepository.save(product);

        Date date = new SimpleDateFormat("yyyy/MM/dd").parse("2019/01/01");
        Review review = new Review("content", date, 99, product);

        reviewRepository.save(review);

		Comment comment = new Comment("content", date, review);

		commentRepository.save(comment);

		List<Comment> comments = commentRepository.findCommentsByReview(review);
		Comment actualComment = comments.get(0);

		assertEquals(1, comments.size());
		assertEquals(comment.getCommentId(), actualComment.getCommentId());
		assertEquals(comment.getContent(), actualComment.getContent());
		assertEquals(comment.getcreatedDate(), actualComment.getcreatedDate());
		assertEquals(comment.getReview(), actualComment.getReview());
	}
}