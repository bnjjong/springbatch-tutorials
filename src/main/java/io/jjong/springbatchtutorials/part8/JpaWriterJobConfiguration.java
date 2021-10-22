/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/17
 */

package io.jjong.springbatchtutorials.part8;

import io.jjong.springbatchtutorials.part5.Person;
import io.jjong.springbatchtutorials.utils.CustomItemReader;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
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
public class JpaWriterJobConfiguration {

  private JobBuilderFactory jobBuilderFactory;
  private StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;

  public JpaWriterJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      EntityManagerFactory entityManagerFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.entityManagerFactory = entityManagerFactory;
  }

  @Bean
  public Job jpaWriterJob() throws Exception {
    return this.jobBuilderFactory.get("jpaWriterJob")
        .incrementer(new RunIdIncrementer())
        .start(this.jpaWriterStep())
        .build();

  }

  @Bean
  public Step jpaWriterStep() throws Exception {
    return this.stepBuilderFactory.get("jpaWriterStep")
        .<Person, Person>chunk(10)
        .reader(itemReader())
        .writer(jpaWriter())
        .build();
  }

  private ItemWriter<Person> jpaWriter() throws Exception {
    JpaItemWriter<Person> itemWriter = new JpaItemWriterBuilder<Person>()
        .entityManagerFactory(entityManagerFactory)
        // merge 하지 않는다 (select를 하여 엔티티가 존재 하는지 체크 하지 않음) 성능상 이점이 있음.
        // 만약 객체의 아이디를 할당 하지 않았다면 userPersist 를 삭제해도 상관 없다.
        // 신규 객체라고 JPA 에서 알아서 인식 함.
        .usePersist(true)
        .build();
    itemWriter.afterPropertiesSet();
    return itemWriter;
  }

  private ItemReader<Person> itemReader() {
    return new CustomItemReader<>(getItems());
  }

  private List<Person> getItems() {
    List<Person> items = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      items.add(new Person("test name-"+i, i+10, "seoul-"+i));
    }
    return items;
  }
}
