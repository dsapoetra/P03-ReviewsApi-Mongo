CREATE TABLE products (
    product_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(300) NOT NULL,
    description TEXT,
    price FLOAT NOT NULL,
    CONSTRAINT price_ck CHECK (price > 0),
    CONSTRAINT product_pk PRIMARY KEY (product_id)
);

INSERT INTO products (name, description, price)
VALUES ('Product A', 'Description of product A', 10.99);
INSERT INTO products (name, description, price)
VALUES ('Prouct B', 'Description of product B', 30.99);

CREATE TABLE reviews (
    review_id INT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL,
    rating INT,
    created_date TIMESTAMP NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT rating_ck CHECK (rating BETWEEN 1 AND 100),
    CONSTRAINT review_pk PRIMARY KEY (review_id),
    CONSTRAINT review_product_fk FOREIGN KEY (product_id) REFERENCES products(product_id)
);

INSERT INTO reviews (content, created_date, rating, product_id)
VALUES ('Review 1 of product A', NOW(), 100, 1);
INSERT INTO reviews (content, created_date, rating, product_id)
VALUES ('Review 2 of product A', NOW(), 99, 1);

INSERT INTO reviews (content, created_date, rating, product_id)
VALUES ('Review 1 of product B', NOW(), 98, 2);
INSERT INTO reviews (content, created_date, rating, product_id)
VALUES ('Review 2 of product B', NOW(), 76, 2);

CREATE TABLE comments (
    comment_id INT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    review_id INT NOT NULL,
    CONSTRAINT comment_pk PRIMARY KEY (comment_id),
    CONSTRAINT comment_review_fk FOREIGN KEY (review_id) REFERENCES reviews(review_id)
);

INSERT INTO comments (content, created_date, review_id)
VALUES ('Comment 1 of review 1 of product A', NOW(), 1);
INSERT INTO comments (content, created_date, review_id)
VALUES ('Comment 2 of review 1 of product A', NOW(), 1);

INSERT INTO comments (content, created_date, review_id)
VALUES ('Comment 1 of review 1 of product B', NOW(), 2);
INSERT INTO comments (content, created_date, review_id)
VALUES ('Comment 2 of review 1 of product B', NOW(), 2);
