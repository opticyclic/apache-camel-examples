package com.github.opticyclic.camel;

import java.nio.file.Path;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

public class PostRoute extends RouteBuilder {
  private final Path input;
  private final Path output;
  private Path error;

  public PostRoute(Path input, Path output, Path error) {
    this.input = input;
    this.output = output;
    this.error = error;
  }

  @Override
  public void configure() throws Exception {
    String in = input.toFile().getAbsolutePath();
    String out = output.toFile().getAbsolutePath();
    String err = error.toFile().getAbsolutePath();

    DataFormat jaxb = new JaxbDataFormat("com.github.opticyclic.camel.model");

    //The delay slows down the polling time from the default of 500ms
    from("file:" + in + "?delay=5s")
      .choice()
      .when(header(Exchange.FILE_NAME).endsWith(".xml"))
        .unmarshal(jaxb)
        .process(new EmployeeDTOProcessor())
        .marshal().json(JsonLibrary.Jackson)
        .to("direct:post")
      .otherwise()
        .log(LoggingLevel.ERROR, "Only XML files accepted. ${file:name} moved to error dir.")
        .to("file:" + err)
        .stop()
      .end();

    from("direct:post")
      .setHeader(Exchange.HTTP_METHOD, constant(HttpPost.METHOD_NAME))
      .setHeader(Exchange.CONTENT_TYPE, constant(ContentType.APPLICATION_JSON))
      .to("http4://localhost:3004/employees?throwExceptionOnFailure=false")
      .choice()
      .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(201))
        .to("file:" + out)
      .otherwise()
        .log("Error response: ${body}")
        .to("file:" + err)
        .stop()
      .end();
  }

}
