/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/10
 */

package io.jjong.springbatchtutorials.part3;

import io.jjong.springbatchtutorials.utils.DateHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

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
@Component
@Slf4j
public class UserProcessor implements Tasklet, StepExecutionListener {
  private List<User> users;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    log.info(">>>>>>>>>>>>>>>>>>> User processor initialized.");
    ExecutionContext executionContext = stepExecution
        .getJobExecution()
        .getExecutionContext();
    this.users = (List<User>) executionContext.get("users");
    log.info(">>>>>>>>>>>>>>>>>>> loading users from context.");
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    users = users.stream().peek(u -> {
      // calculate age;
      LocalDate birth = DateHelper.convertToLocalDateViaInstant(u.getBirthDate());
      LocalDate now = LocalDate.now();
      Period period = Period.between(birth, now);
      int age = period.getYears() + 1; // korean age
      log.info("birth : {}, today : {}, age : {}", birth, now, age);
      u.setAge(age);
    }).toList();

    this.users.forEach(u -> log.info("users : {}", u));
    return RepeatStatus.FINISHED;
  }



  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    stepExecution
        .getJobExecution()
        .getExecutionContext()
        .put("users", this.users);
    log.info(">>>>>>>>>>>>>>>>>>> User processor ended.");
    return ExitStatus.COMPLETED;
  }
}
