package io.jjong.springbatchtutorials;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringbatchTutorialsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringbatchTutorialsApplication.class, args);
  }

}
