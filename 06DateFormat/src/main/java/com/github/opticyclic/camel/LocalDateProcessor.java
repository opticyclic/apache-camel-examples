package com.github.opticyclic.camel;

import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

import com.github.opticyclic.springboot.dto.PayStubDTO;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * This creates a pay stub with a random date on it from the last 70 years
 */
public class LocalDateProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn();
    PayStubDTO payStubDTO = new PayStubDTO();
    LocalDate date = LocalDate.now().minus(Period.ofDays((new Random().nextInt(365 * 70))));
    payStubDTO.setPayDate(date);

    message.setBody(payStubDTO);
  }
}
