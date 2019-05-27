package com.github.opticyclic.camel;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

/**
 * This test demonstrates unmarshalling an XML file, converting to a DTO, then marshalling it into JSON.
 */
public class XmlToJsonTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        DataFormat jaxb = new JaxbDataFormat("com.github.opticyclic.camel.model");

        from("direct:unmarshal")
          .unmarshal(jaxb)
          .process(new EmployeeDTOProcessor())
          .marshal().json(JsonLibrary.Jackson)
          .to("mock:marshalResult");
      }
    };
  }

  @Test
  public void testXmlToJson() throws Exception {
    String request = Files.readFile(new File(this.getClass().getResource("/employee.xml").toURI()));
    String response = template.requestBody("direct:unmarshal", request, String.class);

    log.info(response);
    String expectedJson = "{\"firstName\":\"Montgomery\",\"lastName\":\"Burns\",\"position\":\"CEO\"}";
    Assert.assertEquals(response, expectedJson);
  }
}
