package com.github.opticyclic.camel;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.opticyclic.springboot.dto.PayStubDTO;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.testng.CamelTestSupport;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

/**
 * This test demonstrates using the "yyyy-MM-dd" format to send data to a Sring REST API
 */
public class DateFormatPostTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DataFormat jackson = new JacksonDataFormat(objectMapper, PayStubDTO.class);

        from("direct:start")
          .marshal(jackson)
          .to("direct:post");

        from("direct:post")
          .setHeader(Exchange.HTTP_METHOD, constant(HttpPost.METHOD_NAME))
          .setHeader(Exchange.CONTENT_TYPE, constant(ContentType.APPLICATION_JSON))
          .to("http4://localhost:8080/api/demo/paystubs?throwExceptionOnFailure=false")
          .to("mock:OK");
      }
    };
  }

  @Test
  public void testDateFormat() throws Exception {
    PayStubDTO payStubDTO = new PayStubDTO();
    payStubDTO.setPayDate(LocalDate.of(2019, 4, 1));

    String response = template.requestBody("direct:start", payStubDTO, String.class);
    Assert.assertEquals(response, "{\"payDate\":\"2019-04-01\"}");

    MockEndpoint mockOut = getMockEndpoint("mock:OK");
    mockOut.expectedHeaderReceived(HTTP_RESPONSE_CODE, 200);

    assertMockEndpointsSatisfied();
  }

}
