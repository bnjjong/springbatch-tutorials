/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/17
 */

package io.jjong.springbatchtutorials.part5;

import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create on 2021/10/17. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author Jongsang Han
 * @version 1.0
 * @see
 * @since 1.0
 */
@Configuration
@Slf4j
public class JpaJobConfiguration {

  private JobBuilderFactory jobBuilderFactory;
  private StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;

  public JpaJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      EntityManagerFactory entityManagerFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.entityManagerFactory = entityManagerFactory;
  }

  @Bean
  public Job jpaJob() throws Exception {
    return this.jobBuilderFactory.get("jpaJob")
        .incrementer(new RunIdIncrementer())
        .start(this.jpaStep())
        .build();
  }

  @Bean
  public Step jpaStep() throws Exception {
    return this.stepBuilderFactory.get("jpaStep")
        .<Person, Person>chunk(5)
        .reader(jpaCursorItemReader())
        .writer(print())
        .build();
  }

  private JpaCursorItemReader<Person> jpaCursorItemReader() throws Exception {
    JpaCursorItemReader<Person> itemReader = new JpaCursorItemReaderBuilder<Person>()
        .name("jpaCursorReader")
        .entityManagerFactory(entityManagerFactory)
        .queryString("select p from Person p")
        .build();
    itemReader.afterPropertiesSet();
    return itemReader;

  }

  private ItemWriter<Person> print() {

    return items -> {
      log.info("writer is called!");
      items.forEach(person -> log.info("person : {}", person));
    };
  }
}
