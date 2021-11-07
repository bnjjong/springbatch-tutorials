package io.jjong.springbatchtutorials.part4;/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/16
 */

import static org.assertj.core.api.Assertions.assertThat;

import io.jjong.springbatchtutorials.TestBatchConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

/**
 * <a href='https://jojoldu.tistory.com/455'>참고 링크</a>
 * spring 4.1 에서 새롭게 추가 됨.
 * 자동으로 ApplicationContext 에서 테스트에 필요한 여러 Util bean 을 등록 함.
 * - JobLauncherTestUtils
 *  - 스프링 배치 테스트에 필요한 전반적인 유틸 기능들을 지원
 * - JobRepositoryTestUtils
 *  - DB에 생성된 JobExecution을 쉽게 생성/삭제 가능하게 지원
 * - StepScopeTestExecutionListener
 *  - 배치 단위 테스트시 StepScope 컨텍스트 생성
 *  - 해당 컨텍스트를 통해 JobParameter등을 단위 테스트에서 DI 받을 수 있음.
 * - JobScopeTestExecutionListener
 *  - 배치 단위 테스트시 JobScope 컨텍스트를 생성
 *  - 해당 컨텍스트를 통해 JobParameter등을 단위 테스트에서 DI 받을 수 있음.
 */

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@ContextConfiguration(classes = {ChunkCsvTaskJobConfiguration.class, TestBatchConfiguration.class})
class ChunkCsvTaskJobConfigurationTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Test
  @DisplayName("do test")
  public void test_allow_duplicate() throws Exception {
    // given
    JobParameters jobParameters = new JobParametersBuilder()
//        .addString("allow_duplicate", "false")
        .toJobParameters();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // then
    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    assertThat(jobExecution.getStepExecutions().stream() // step 은 여러개 이므로
        .mapToInt(StepExecution::getWriteCount) // 추가로 commitCount, readCount 등도 확인 할 수 있다.
        .sum()
    ).isEqualTo(5);

  }
}