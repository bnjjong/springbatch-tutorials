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
import org.springframework.batch.core.ExitStatus;
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
public class ConditionJobConfiguration {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job conditionJob() {
    return this.jobBuilderFactory.get("conditionJob")
        .incrementer(new RunIdIncrementer())
        .start(step1())
          .on("FAILED")
          .to(step2())
          .on("*")
          .end()
        .from(step1())
          .on("*")
          .to(step3())// step1
          .on("*")
          .end()
        .end()
        .build();
  }

  public Step step3() {
    return stepBuilderFactory.get("step3")
        .tasklet((contribution, chunkContext) -> {
          log.info("this is step3!!!");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2")
        .tasklet((contribution, chunkContext) -> {
          log.info("this is step2!!");
          return RepeatStatus.FINISHED;
        })
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .tasklet((contribution, chunkContext) -> {
          log.info("this is step1");
//          contribution.setExitStatus(ExitStatus.FAILED);
          return RepeatStatus.FINISHED;
        })
        .build();
  }
}
