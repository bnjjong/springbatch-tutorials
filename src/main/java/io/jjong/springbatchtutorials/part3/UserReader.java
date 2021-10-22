/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/10
 */

package io.jjong.springbatchtutorials.part3;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.jjong.springbatchtutorials.utils.FileHelper;
import io.jjong.springbatchtutorials.utils.FileReadException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
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
public class UserReader implements Tasklet, StepExecutionListener {
  private FileReader reader;
  private List<User> users;


  @Override
  public void beforeStep(StepExecution stepExecution) {
    try {
      log.info(">>>>>>>>>>>>>>>>>>> User reader initialized.");
      log.info(">>>>>>>>>>>>>>>>>>> start to read csv file.");
      this.reader = new FileReader(FileHelper.getFileFromResource("part3-user.csv"));
      log.info(">>>>>>>>>>>>>>>>>>> end to read csv file.");
    } catch (FileNotFoundException | FileReadException e) {
      e.printStackTrace();
    }

  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    log.info(">>>>>>>>>>>>>>>>>>> start execute");

    users = new CsvToBeanBuilder(this.reader)
        .withType(User.class)
        .withSeparator(',')
        .build()
        .parse();
    users.stream().forEach(user -> log.info(">>>>>>>>>>>>>>>>>>> user : {}", user));

    return RepeatStatus.FINISHED;
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    stepExecution
        .getJobExecution()
        .getExecutionContext()
        .put("users", this.users);
    log.info(">>>>>>>>>>>>>>>>>>> User reader ended.");
    return ExitStatus.COMPLETED;
  }
}
