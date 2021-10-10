/*
 * Created By dogfootmaster@gmail.com on 2021
 * This program is free software
 *
 * @author <a href=“mailto:dogfootmaster@gmail.com“>Jongsang Han</a>
 * @since 2021/10/10
 */

package io.jjong.springbatchtutorials.part3;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@Getter
@Setter
@ToString
@NoArgsConstructor
// 스텝간에 공유하며 사용하기 위해서는 직렬화는 필수.
public class User implements Serializable {

//  @CsvBindByPosition(position = 1)
  @CsvBindByName(column = "Identifier", required = true)
  private long id;

//@CsvBindByPosition(position = 0)
  @CsvBindByName(column = "Login email", required = true)
  private String email;

  @CsvBindByName(column = "First name")
  private String firstName;

  @CsvBindByName(column = "Last name")
  private String lastName;

  @CsvBindByName(column = "Birth date")
  @CsvDate("yyyy/MM/dd")
  private Date birthDate;

  @CsvBindByName(column = "age")
  private int age;

}
