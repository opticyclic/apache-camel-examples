package com.github.opticyclic.springboot.dto;

import java.time.LocalDate;
import java.util.Objects;

public class PayStubDTO {
  private LocalDate payDate;

  public LocalDate getPayDate() {
    return payDate;
  }

  public void setPayDate(LocalDate payDate) {
    this.payDate = payDate;
  }

  @Override
  public String toString() {
    return "PayStubDTO{" +
             "payDate=" + payDate +
             '}';
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    PayStubDTO that = (PayStubDTO)o;
    return Objects.equals(payDate, that.payDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payDate);
  }
}
