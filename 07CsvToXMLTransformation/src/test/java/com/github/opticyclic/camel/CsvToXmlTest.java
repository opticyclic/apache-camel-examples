package com.github.opticyclic.camel;

import java.io.File;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;

import com.github.opticyclic.camel.model.XmlEmployee;
import com.github.opticyclic.camel.processors.ProcessEmployee;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.annotations.Test;

/**
 * This test demonstrates unmarshalling a CSV then marshalling it into XML.
 * The CSV file is put on the Exchange and converted to a List of Employee objects using Camel Bindy.
 * The list is then split into individual Employee objects
 * Each Employee object is then converted to XML using JAXB.
 */
public class CsvToXmlTest extends CamelTestSupport {

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    return new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        // Initialize JAXB
        JAXBContext jaxbContext = JAXBContext.newInstance(XmlEmployee.class);
        JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(jaxbContext);

        DataFormat bindy = new BindyCsvDataFormat(Employee.class);

        from("direct:start")
          .unmarshal(bindy)
          .split(body())
          .process(new ProcessEmployee())
          .marshal(jaxbDataFormat)
          .log("\n#####\nTransformed XML: \n${body}\n#####\n")
          .to("file:" + new File("data").getAbsolutePath());

      }
    };
  }

  @Test
  public void testCsvToXml() throws Exception {
    File testFile = new File(this.getClass().getResource("/employees.csv").toURI());
    String content = new Scanner(testFile).useDelimiter("\\Z").next();
    template.requestBody("direct:start", content, String.class);

    log.info("Complete");
  }
}
