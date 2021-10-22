/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/16
 */

package io.jjong.springbatchtutorials.part4;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * create on 2021/10/16. create by IntelliJ IDEA.
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
public class ChunkCsvTaskJobConfiguration {

  private JobBuilderFactory jobBuilderFactory;
  private StepBuilderFactory stepBuilderFactory;

  public ChunkCsvTaskJobConfiguration(
      JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  public Job csvChunkJob() throws Exception {
    return this.jobBuilderFactory.get("csvChunkJob")
        .incrementer(new RunIdIncrementer())
        .start(this.csvChunkStep())
        .build();
  }

  private Step csvChunkStep() throws Exception {
    return this.stepBuilderFactory.get("csvChunkStep")
        .<Person,Person>chunk(5)
        .reader(csvFileItemReader())
        .writer(itemWriter())
        .build();
  }

  private FlatFileItemReader<Person> csvFileItemReader() throws Exception {
    // csv 를 한 줄씩 읽을 수 있는 LineMapper
    DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setNames("id","name","age","address");
    lineMapper.setLineTokenizer(tokenizer);

    lineMapper.setFieldSetMapper(fieldSet -> {
      int id = fieldSet.readInt("id");
      String name = fieldSet.readString("name");
      int age = fieldSet.readInt("age");
      String address = fieldSet.readString("address");
      return new Person(id, name, age, address);
    });

    FlatFileItemReader<Person> itemReader = new FlatFileItemReaderBuilder<Person>()
        .name("csvFileReader")
        .encoding("UTF-8")
        .resource(new ClassPathResource("part4-user.csv")) // resource 폴더 아래에 파일을 읽을 수 있도록 해준다.
        .linesToSkip(1) // header는 스킵
        .lineMapper(lineMapper)
        .build();
    itemReader.afterPropertiesSet(); // 필수 설정 값이 정상적으로 되어 있는지 검증 해준다.
    return itemReader;
  }

  private ItemWriter<Person> itemWriter() {
//    return items -> log.info(items.stream()
//        .map(Person::getName)
//        .collect(Collectors.joining(", ")));
    return items -> {
      items.forEach(i -> {
        log.info("person : {}", i);
      });
    };
  }
}
