package com.github.opticyclic.camel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is demo takes a CSV and converts it to JSON.
 */
public class CsvToJsonDemo {

  private static Logger logger = LoggerFactory.getLogger(CsvToJsonDemo.class);

  public static void main(String[] args) throws Exception {
    Path dataDir = Paths.get("data");
    Path input = dataDir.resolve("input");
    Path output = dataDir.resolve("output");
    Path error = dataDir.resolve("error");

    CsvToJsonDemo demo = new CsvToJsonDemo();
    demo.boot(input, output, error);
  }

  private void boot(Path input, Path output, Path error) throws Exception {
    //Create directories for the file transfer
    Files.createDirectories(input);
    logger.info("Created directory " + input.toAbsolutePath());
    Files.createDirectories(output);
    logger.info("Created directory " + output.toAbsolutePath());
    Files.createDirectories(error);
    logger.info("Created directory " + error.toAbsolutePath());

    //Use the Camel Main instance instead of manually creating a CamelContext
    Main main = new Main();

    //Add a route to process the input files
    RouteBuilder routeBuilder = new CsvToJsonRoute(input, output, error);
    main.addRouteBuilder(routeBuilder);

    //This will keep Camel running until you terminate the JVM
    main.run();
  }

}
