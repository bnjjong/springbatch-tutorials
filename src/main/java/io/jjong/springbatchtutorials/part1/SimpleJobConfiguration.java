/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/07
 */

package io.jjong.springbatchtutorials.part1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class SimpleJobConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final SimpleTasklet simpleTasklet;

  @Bean
  public Job simpleJob() {
    return jobBuilderFactory.get("simpleJob")
        .incrementer(new RunIdIncrementer())
        .start(firstStep())
        .next(secondStep())
        .build();
  }

  public Step secondStep() {
    return stepBuilderFactory.get("secondStep")
        .tasklet((c, cc) -> {
          log.info("this is second tasklet!");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean
  public Step firstStep() {
    return stepBuilderFactory.get("firstStep")
        .tasklet(simpleTasklet)
        .build();
  }
}
