/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/22
 */

package io.jjong.springbatchtutorials.part9;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * create on 2021/10/22. create by IntelliJ IDEA.
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
public class AsyncConfiguration {
  private static final int CHUNK_SIZE = 10;
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final DataSource dataSource;

  @Bean
  public Job asyncJob() throws Exception {
    return this.jobBuilderFactory.get("asyncJob")
        .incrementer(new RunIdIncrementer())
//        .start(this.syncStep())
        .start(this.asyncStep())
        .listener(new StopWatchJobListener())
        .build();
  }

  @Bean
  public Step syncStep() {
    return this.stepBuilderFactory.get("syncStep")
        .<Customer,Customer>chunk(CHUNK_SIZE)
        .reader(customerPagingReader())
        .processor(customItemProcessor())
        .writer(customItemWriter())
        .taskExecutor(new SimpleAsyncTaskExecutor())
        .build();
  }

  @Bean
  public Step asyncStep() throws Exception {
    return this.stepBuilderFactory.get("asyncStep")
        .<Customer,Customer>chunk(CHUNK_SIZE)
        .reader(customerPagingReader())
        .processor(asyncCustomerItemProcessor())
        .writer(asyncCustomerItemWriter())
//        .taskExecutor(taskExecutor())
//        .taskExecutor(new SimpleAsyncTaskExecutor())
        .build();
  }

  @Bean
  public JdbcPagingItemReader<Customer> customerPagingReader() {
    JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

    reader.setDataSource(this.dataSource);
    reader.setPageSize(CHUNK_SIZE);
    reader.setFetchSize(CHUNK_SIZE);
    reader.setRowMapper((rs, rowNum) -> new Customer(
        rs.getLong("id"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        new Date(rs.getDate("birth_date").getTime()))
    );

    MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
    queryProvider.setSelectClause("id, first_name, last_name, birth_date");
    queryProvider.setFromClause("from customer");

    Map<String, Order> sortKeys = new HashMap<>(1);
    sortKeys.put("id", Order.ASCENDING);
    queryProvider.setSortKeys(sortKeys);
    reader.setQueryProvider(queryProvider);

    return reader;
  }


  @Bean
  public ItemProcessor customItemProcessor() {
    return new ItemProcessor<Customer, Customer>() {
      @Override
      public Customer process(Customer item) throws Exception {
        Thread.sleep(10);
        // business 수행
        item.setAndCalculateAge();
        item.toupperFirstName();
        item.toupperLastName();
        item.setFullname();
        return item;
      }
    };
  }

  @Bean
  public JdbcBatchItemWriter customItemWriter() {
    JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();

    itemWriter.setDataSource(this.dataSource);
    itemWriter.setSql("insert into customer2 values (:id, :firstName, :lastName, :fullName, :birthDate, :age)");
    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
    itemWriter.afterPropertiesSet();

    return itemWriter;
  }



  @Bean
  public AsyncItemProcessor asyncCustomerItemProcessor() throws Exception {
    AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor();

    asyncItemProcessor.setDelegate(customItemProcessor());
    asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        asyncItemProcessor.setTaskExecutor(taskExecutor());
    asyncItemProcessor.afterPropertiesSet();

    return asyncItemProcessor;
  }

  @Bean
  public AsyncItemWriter asyncCustomerItemWriter() throws Exception {
    AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();

    asyncItemWriter.setDelegate(customItemWriter());
    asyncItemWriter.afterPropertiesSet();

    return asyncItemWriter;
  }



  @Bean
  public TaskExecutor taskExecutor(){
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(8);
    executor.setThreadNamePrefix("async-thread-");
    return executor;
  }


}
