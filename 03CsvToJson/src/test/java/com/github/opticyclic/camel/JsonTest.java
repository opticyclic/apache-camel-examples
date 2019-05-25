package com.github.opticyclic.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test demonstrates marshalling objects into JSON Strings using Jackson.
 */
public class JsonTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        from("direct:marshal")
          .marshal().json(JsonLibrary.Jackson)
          .to("mock:marshalResult");
      }
    };
  }

  @Test
  public void testJsonMarshal() throws Exception {
    Employee employee = new Employee();
    employee.setFirstName("Christine");
    employee.setLastName("Brakus");
    employee.setEmail("Christine_Brakus6@hotmail.com");

    String response = template.requestBody("direct:marshal", employee, String.class);

    Assert.assertEquals("{\"firstName\":\"Christine\",\"lastName\":\"Brakus\",\"email\":\"Christine_Brakus6@hotmail.com\"}", response);
  }

}
