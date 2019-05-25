package com.github.opticyclic.camel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This test demonstrates unmarshalling a CSV then marshalling it into JSON.
 * <li>The CSV file is created in the input folder and then loaded into the Exchange.</li>
 * <li>It is then converted to a List of Employee objects using Camel Bindy.</li>
 * <li>The Employee objects are then converted to JSON using Jackson.</li>
 * <li>The JSON is then put in the output directory.</li>
 * This test uses temp directories to test the full route.
 * The AfterClass method cleans up any files that were created during the test.
 */
public class CsvToJsonRouteTest extends CamelTestSupport {
  private Path data;
  private Path input;
  private Path output;
  private Path error;

  @BeforeClass
  public void setup() throws IOException {
    data = Files.createTempDirectory("data");
    input = data.resolve("input");
    output = data.resolve("output");
    error = data.resolve("error");
    Files.createDirectories(input);
    Files.createDirectories(output);
    Files.createDirectories(error);
  }

  @AfterClass
  public void tearDown() throws IOException {
    Files.walk(data)
      .sorted(Comparator.reverseOrder())
      .map(Path::toFile)
      .peek(System.out::println)
      .forEach(File::delete);
  }

  @Override
  protected RoutesBuilder createRouteBuilder() throws Exception {
    return new CsvToJsonRoute(input, output, error);
  }

  @Test
  public void convertCsvToJson() throws Exception {
    Path testCsv = Paths.get(this.getClass().getResource("/employees.csv").toURI());
    Files.copy(testCsv, input.resolve(testCsv.getFileName()));

    //Give Camel time to poll
    Thread.sleep(2000);
    Path result = output.resolve("employees.csv");
    Assert.assertTrue(Files.exists(result));

    String expectedJson = "[" +
                            "{\"firstName\":\"Christine\",\"lastName\":\"Brakus\",\"email\":\"Christine_Brakus6@hotmail.com\"}," +
                            "{\"firstName\":\"Harold\",\"lastName\":\"Murray\",\"email\":\"Harold.Murray@gmail.com\"}," +
                            "{\"firstName\":\"Allison\",\"lastName\":\"Mayer\",\"email\":\"Allison.Mayer79@hotmail.com\"}" +
                            "]";
    String actualJson = new String(Files.readAllBytes(result));
    Assert.assertEquals(expectedJson, actualJson);
  }

}