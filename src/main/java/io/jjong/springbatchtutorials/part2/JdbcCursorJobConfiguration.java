/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/07
 */

package io.jjong.springbatchtutorials.part2;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * create on 2021/10/07. create by IntelliJ IDEA.
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
@RequiredArgsConstructor
public class JdbcCursorJobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource;

  private static final int chunkSize = 5;

  @Bean
  public Job jdbcCursorJob() {
    return jobBuilderFactory.get("jdbcCursorJob")
        .incrementer(new RunIdIncrementer())
        .start(step1())
        .build();
  }

  private Step step1() {
    return stepBuilderFactory.get("jdbcStep1")
        .<Person, Person>chunk(chunkSize)
        .reader(jdbcReader())
        .processor(baseProcessor())
        .writer(print())
        .build();
  }

  private ItemProcessor<Person,Person> baseProcessor() {
    return item -> {
      log.info("processor is called! >>>>> {}", item);
      return item;
    };
  }

  private ItemWriter<Person> print() {

    return items -> {
      log.info("writer is called!");
      items.forEach(System.out::println);
    };
  }

  public ItemReader<Person> jdbcReader() {
    return new JdbcCursorItemReaderBuilder<Person>()
        .fetchSize(chunkSize)
        .dataSource(dataSource)
        .rowMapper(new BeanPropertyRowMapper<>(Person.class))
        .sql("select name, age, address from person")
        .name("person reader")
        .build();
  }


}
