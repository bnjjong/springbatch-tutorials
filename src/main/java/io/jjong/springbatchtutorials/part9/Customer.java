
package io.jjong.springbatchtutorials.part9;

import io.jjong.springbatchtutorials.utils.DateHelper;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
public class Customer {

  private long id;
  @NonNull
  private String firstName;
  @NonNull
  private String lastName;
  @NonNull
  private Date birthDate;
  private String fullName;
  private int age;

  public Customer(long id, String firstName, String lastName, Date birthdate) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthdate;
  }

  public void setFullname() {
    this.fullName = String.format("%s %s", lastName, firstName);
  }

  public void setAndCalculateAge() {
    LocalDate birth = DateHelper.convertToLocalDateViaInstant(this.birthDate);
    LocalDate now = LocalDate.now();
    Period period = Period.between(birth, now);
    this.age = period.getYears() + 1; // korean age
    log.info("birth : {}, today : {}, age : {}", birth, now, age);
  }

  public void toupperFirstName() {
    this.firstName = this.firstName.toUpperCase();
  }

  public void toupperLastName() {
    this.lastName = this.lastName.toUpperCase();
  }
}
