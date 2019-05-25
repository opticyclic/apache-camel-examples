package com.github.opticyclic.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.annotations.Test;

/**
 * This simple test uses a direct in and a mock out instead file in and out to test the Processor by itself.
 */
public class TextProcessorTest extends CamelTestSupport {

  private static final String ROUTE_IN = "direct:in";
  private static final String ROUTE_OUT = "mock:out";

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        from(ROUTE_IN)
          .process(new TextProcessor())
          .to(ROUTE_OUT);
      }
    };
  }

  @Test
  public void testProcess() throws Exception {
    String inputText = "Input Text";
    template.sendBody(ROUTE_IN, inputText);

    MockEndpoint mockOut = getMockEndpoint(ROUTE_OUT);
    mockOut.message(0).body().startsWith(inputText);
    mockOut.message(0).body().contains("This is the new content");

    assertMockEndpointsSatisfied();
  }

}