/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/17
 */

package io.jjong.springbatchtutorials.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

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
public class CustomItemReader<T> implements ItemReader<T> {
  private final List<T> items;

  public CustomItemReader(List<T> items) {
    this.items = new ArrayList<>(items);
  }

  @Override
  public T read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (!items.isEmpty()) {
      return items.remove(0);
    }
    return null;
  }
}
