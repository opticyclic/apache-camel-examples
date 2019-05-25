package com.github.opticyclic.camel;

import java.io.File;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

/**
 * This test demonstrates unmarshalling a CSV then marshalling it into JSON.
 * The CSV file is put on the Exchange and converted to a List of Employee objects using Camel Bindy.
 * The Employee objects are then converted to JSON using Jackson.
 */
public class CsvToJsonTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        final DataFormat bindy = new BindyCsvDataFormat(Employee.class);

        from("direct:unmarshal")
          .unmarshal(bindy)
          .marshal().json(JsonLibrary.Jackson)
          .to("mock:marshalResult");
      }
    };
  }

  @Test
  public void testCsvToJson() throws Exception {
    String request = Files.readFile(new File(this.getClass().getResource("/employees.csv").toURI()));
    String response = template.requestBody("direct:unmarshal", request, String.class);

    log.info(response);
    String expectedJson = "[" +
                            "{\"firstName\":\"Christine\",\"lastName\":\"Brakus\",\"email\":\"Christine_Brakus6@hotmail.com\"}," +
                            "{\"firstName\":\"Harold\",\"lastName\":\"Murray\",\"email\":\"Harold.Murray@gmail.com\"}," +
                            "{\"firstName\":\"Allison\",\"lastName\":\"Mayer\",\"email\":\"Allison.Mayer79@hotmail.com\"}" +
                            "]";
    Assert.assertEquals(expectedJson, response);
  }
}
