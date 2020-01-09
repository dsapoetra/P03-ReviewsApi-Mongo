package com.udacity.course3.reviews.config;

import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

@Configuration
public class ApplicationConfig {
  @Bean
  public MongoClientOptions getMongoClientOptions() {
    return MongoClientOptions.builder().build();
  }

  @Bean(name = "mongoTemplate")
  public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter converter) {
    return new MongoTemplate(mongoDbFactory, converter);
  }
}
