package com.github.opticyclic.camel;

import java.nio.file.Path;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;

public class CsvToJsonRoute extends RouteBuilder {
  private final Path input;
  private final Path output;
  private Path error;

  public CsvToJsonRoute(Path input, Path output, Path error) {
    this.input = input;
    this.output = output;
    this.error = error;
  }

  @Override
  public void configure() throws Exception {
    String in = input.toFile().getAbsolutePath();
    String out = output.toFile().getAbsolutePath();
    String err = error.toFile().getAbsolutePath();

    DataFormat bindy = new BindyCsvDataFormat(Employee.class);

    //The delay slows down the polling time from the default of 500ms
    from("file:" + in + "?delay=5s")
      .choice()
      .when(header(Exchange.FILE_NAME).endsWith(".csv"))
        .unmarshal(bindy)
        .marshal().json(JsonLibrary.Jackson)
        .to("file:" + out)
      .otherwise()
        .log(LoggingLevel.ERROR, "Only CSV files accepted. ${file:name} moved to error dir.")
        .to("file:" + err)
        .stop()
      .end();
  }

}
