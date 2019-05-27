package com.github.opticyclic.camel;

import java.io.File;

import com.github.opticyclic.camel.model.Address;
import com.github.opticyclic.camel.model.Employee;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

/**
 * This test demonstrates unmarshalling an XML file using JAXB.
 */
public class JaxbTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {

    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        DataFormat jaxb = new JaxbDataFormat("com.github.opticyclic.camel.model");

        from("direct:unmarshal")
          .unmarshal(jaxb)
          .to("mock:unmarshalResult");
      }
    };
  }

  @Test
  public void testXMLUnmarshal() throws Exception {
    String request = Files.readFile(new File(this.getClass().getResource("/employee.xml").toURI()));
    Employee actualEmployee = template.requestBody("direct:unmarshal", request, Employee.class);

    Employee expectedEmployee = new Employee();
    expectedEmployee.setId(1);
    expectedEmployee.setFirstName("Montgomery");
    expectedEmployee.setLastName("Burns");
    Address address =new Address();
    address.setAddressLine1("1000 Mammon Lane");
    address.setCity("Springfield");
    address.setState("DC");
    address.setPostalCode("1000");
    address.setCountry("US");
    expectedEmployee.setAddress(address);
    expectedEmployee.setPosition("CEO");

    Assert.assertEquals(expectedEmployee, actualEmployee);
  }
}
