package com.github.opticyclic.camel;

import java.nio.file.Path;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;

public class XmlToJsonRoute extends RouteBuilder {
  private final Path input;
  private final Path output;
  private Path error;

  public XmlToJsonRoute(Path input, Path output, Path error) {
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
        .to("file:" + out)
      .otherwise()
        .log(LoggingLevel.ERROR, "Only XML files accepted. ${file:name} moved to error dir.")
        .to("file:" + err)
        .stop()
      .end();
  }

}
