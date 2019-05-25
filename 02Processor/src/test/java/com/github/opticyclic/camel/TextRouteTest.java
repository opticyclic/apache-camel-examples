package com.github.opticyclic.camel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.testng.CamelTestSupport;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This test uses temp directories to test the full route.
 * The AfterClass method cleans up any files that were created during the test.
 */
public class TextRouteTest extends CamelTestSupport {
  private Path data;
  private Path input;
  private Path output;

  @BeforeClass
  public void setup() throws IOException {
    data = Files.createTempDirectory("data");
    input = data.resolve("input");
    output = data.resolve("output");
    Files.createDirectories(input);
    Files.createDirectories(output);
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
    return new TextRoute(input, output);
  }

  @Test
  public void checkFileExistsInOutputDirectory() throws Exception {
    String testFile = "test.txt";
    Files.createFile(input.resolve(testFile));
    //Give Camel time to poll
    Thread.sleep(2000);
    Assert.assertTrue(Files.exists(output.resolve(testFile)));
  }

}