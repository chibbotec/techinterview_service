package com.ll.techinterview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableJpaAuditing
public class TechInterviewApplication {

  public static void main(String[] args) {
    SpringApplication.run(TechInterviewApplication.class, args);
  }

}
