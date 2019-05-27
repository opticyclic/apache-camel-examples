package com.github.opticyclic.camel;

import java.util.Objects;

public class EmployeeDTO {

  private String firstName;

  private String lastName;

  private String position;

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

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "EmployeeDTO{" +
             "firstName='" + firstName + '\'' +
             ", lastName='" + lastName + '\'' +
             ", position='" + position + '\'' +
             '}';
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;

    EmployeeDTO employeeDTO = (EmployeeDTO)o;

    if(!firstName.equals(employeeDTO.firstName)) return false;
    if(!lastName.equals(employeeDTO.lastName)) return false;
    return position.equals(employeeDTO.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, position);
  }
}
