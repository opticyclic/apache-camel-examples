package com.github.opticyclic.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.opticyclic.springboot.dto.PayStubDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

public class DateFormatRoute extends RouteBuilder {

  private String period;

  public DateFormatRoute(String period) {
    this.period = period;
  }

  @Override
  public void configure() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    DataFormat jackson = new JacksonDataFormat(objectMapper, PayStubDTO.class);

    from("timer:foo?period=" + period)
      .process(new LocalDateProcessor())
      .marshal(jackson)
      .setHeader(Exchange.HTTP_METHOD, constant(HttpPost.METHOD_NAME))
      .setHeader(Exchange.CONTENT_TYPE, constant(ContentType.APPLICATION_JSON))
      .to("log:${body}")
      .to("http4://localhost:8080/api/demo/paystubs?throwExceptionOnFailure=false");
  }

}
