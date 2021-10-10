/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/10
 */

package io.jjong.springbatchtutorials.part3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create on 2021/10/10. create by IntelliJ IDEA.
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
public class TaskletAdvanceConfiguration {
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  // Tasklet
  private final UserReader userReader;
  private final UserProcessor userProcessor;
  private final UserWriter userWriter;


  @Bean
  public Job advancedTaskletJob() {
    return jobBuilderFactory.get("advancedTaskletJob")
        .incrementer(new RunIdIncrementer())
        .start(readUsers())
        .next(processUsers())
        .next(writeUsers())
        .build();
  }

  private Step writeUsers() {
    return stepBuilderFactory.get("writeUsers")
        .tasklet(userWriter)
        .build();
  }

  private Step processUsers() {
    return stepBuilderFactory.get("processUsers")
        .tasklet(userProcessor)
        .build();
  }

  private Step readUsers() {
    return stepBuilderFactory.get("readUsers")
        .tasklet(userReader)
        .build();
  }


}
