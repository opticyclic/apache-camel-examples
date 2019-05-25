package com.github.opticyclic.camel;

import java.io.File;
import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

/**
 * This test demonstrates unmarshalling a CSV using Camel Bindy.
 * The CSV file is put on the Exchange and converted to a List of Employee objects.
 */
public class CsvTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        final DataFormat bindy = new BindyCsvDataFormat(Employee.class);

        from("direct:unmarshal")
          .unmarshal(bindy)
          .to("mock:unmarshalResult");
      }
    };
  }

  @Test
  public void testCsvUnmarshal() throws Exception {
    String request = Files.readFile(new File(this.getClass().getResource("/employees.csv").toURI()));
    List<Employee> employeeList = template.requestBody("direct:unmarshal", request, List.class);

    Employee expectedEmployee = new Employee();
    expectedEmployee.setFirstName("Christine");
    expectedEmployee.setLastName("Brakus");
    expectedEmployee.setEmail("Christine_Brakus6@hotmail.com");

    Employee actualEmployee = employeeList.get(0);
    Assert.assertEquals(expectedEmployee, actualEmployee);
  }
}
