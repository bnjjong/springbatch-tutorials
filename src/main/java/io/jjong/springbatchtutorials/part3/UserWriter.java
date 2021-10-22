/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/10
 */

package io.jjong.springbatchtutorials.part3;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.FileWriter;
import java.io.IOException;
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
public class UserWriter implements Tasklet, StepExecutionListener {
  private List<User> users;
  private CSVWriter writer;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    log.info(">>>>>>>>>>>>>>>>>>> User writer initialized.");
    ExecutionContext executionContext = stepExecution
        .getJobExecution()
        .getExecutionContext();

    this.users = (List<User>) executionContext.get("users");
    log.info(">>>>>>>>>>>>>>>>>>> loading users from context.");

    try {
      this.writer = new CSVWriter(new FileWriter("new-part3-user.csv"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    log.info(">>>>>>>>>>>>>>>>>>> Execute to write users");
    StatefulBeanToCsv<User> beanToCsv = new StatefulBeanToCsvBuilder<User>(writer)
        .build();
    beanToCsv.write(users);
    writer.close();
    return RepeatStatus.FINISHED;
  }



  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info(">>>>>>>>>>>>>>>>>>> User writer ended.");
    return ExitStatus.COMPLETED;
  }
}
