package com.github.opticyclic.camel;

import java.util.Objects;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",", skipFirstLine = true)
public class Employee {

  @DataField(pos = 1)
  private String firstName;

  @DataField(pos = 2)
  private String lastName;

  @DataField(pos = 3)
  private String email;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "Employee{" +
             "firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", email='" + email + '\'' +
             '}';
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;

    Employee employee = (Employee)o;

    if(!firstName.equals(employee.firstName)) return false;
    if(!lastName.equals(employee.lastName)) return false;
    return email.equals(employee.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, email);
  }
}
