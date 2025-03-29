package com.ll.techinterview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class TechInterviewApplication {

  public static void main(String[] args) {
    SpringApplication.run(TechInterviewApplication.class, args);
  }

}
