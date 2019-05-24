package com.github.opticyclic.camel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is demo copies files from the input to the output directory.
 * In between it modifies the file using a processor.
 */
public class ProcessorDemo {

  private static Logger logger = LoggerFactory.getLogger(ProcessorDemo.class);

  public static void main(String[] args) throws Exception {
    Path dataDir = Paths.get("data");
    Path input = dataDir.resolve("input");
    Path output = dataDir.resolve("output");

    ProcessorDemo demo = new ProcessorDemo();
    demo.boot(input, output);
  }

  private void boot(Path input, Path output) throws Exception {
    //Create directories for the file transfer
    Files.createDirectories(input);
    logger.info("Created directory " + input.toAbsolutePath());
    Files.createDirectories(output);
    logger.info("Created directory " + output.toAbsolutePath());

    //Use the Camel Main instance instead of manually creating a CamelContext
    Main main = new Main();

    //Add a route to copy and modify the file
    RouteBuilder routeBuilder = new TextRoute(input, output);
    main.addRouteBuilder(routeBuilder);

    //This will keep Camel running until you terminate the JVM
    main.run();
  }

}
