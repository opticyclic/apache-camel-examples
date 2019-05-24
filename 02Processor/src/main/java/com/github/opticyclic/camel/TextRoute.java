package com.github.opticyclic.camel;

import java.nio.file.Path;

import org.apache.camel.builder.RouteBuilder;

public class TextRoute extends RouteBuilder {
  private final Path input;
  private final Path output;

  public TextRoute(Path input, Path output) {
    this.input = input;
    this.output = output;
  }

  @Override
  public void configure() throws Exception {
    String in = input.toFile().getAbsolutePath();
    String out = output.toFile().getAbsolutePath();

    //The delay slows down the polling time from the default of 500ms
    from("file:" + in + "?delay=5s")
      .process(new TextProcessor())
      .to("file:" + out);
  }

}
