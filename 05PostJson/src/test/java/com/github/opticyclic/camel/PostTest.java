package com.github.opticyclic.camel;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.testng.CamelTestSupport;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

/**
 * This test demonstrates unmarshalling an XML file, converting to a DTO, then marshalling it into JSON and finally sending it to a web api.
 */
public class PostTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        DataFormat jaxb = new JaxbDataFormat("com.github.opticyclic.camel.model");

        from("direct:start")
          .unmarshal(jaxb)
          .process(new EmployeeDTOProcessor())
          .marshal().json(JsonLibrary.Jackson)
          .to("direct:post");

        from("direct:post")
          .setHeader(Exchange.HTTP_METHOD, constant(HttpPost.METHOD_NAME))
          .setHeader(Exchange.CONTENT_TYPE, constant(ContentType.APPLICATION_JSON))
            .to("http4://localhost:3004/employees?throwExceptionOnFailure=false")
          .choice()
          .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(201))
            .to("mock:OK")
          .otherwise()
            .log("Error response: ${body}")
            .to("mock:ERROR")
            .stop()
          .end();
      }
    };
  }

  @Test
  public void testGoodMessage() throws Exception {
    String request = Files.readFile(new File(this.getClass().getResource("/employee.xml").toURI()));
    template.requestBody("direct:start", request);

    MockEndpoint mockOut = getMockEndpoint("mock:OK");
    mockOut.expectedHeaderReceived(HTTP_RESPONSE_CODE, 201);

    assertMockEndpointsSatisfied();
  }

  @Test
  public void testBadMessage() throws Exception {
    String request = "<Invalid>JSON</>";
    template.requestBody("direct:post", request);

    MockEndpoint mockError = getMockEndpoint("mock:ERROR");
    mockError.expectedHeaderReceived(HTTP_RESPONSE_CODE, 400);

    assertMockEndpointsSatisfied();
  }
}
