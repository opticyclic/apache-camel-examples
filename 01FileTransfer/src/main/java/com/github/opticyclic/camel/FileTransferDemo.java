package com.github.opticyclic.camel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple demo that copies files from the input to the output directory.
 * Run the main class then put a file in the data/input directory.
 * Once the copy is complete the input file is moved to a hidden dir called .camel in the input directory.
 */
public class FileTransferDemo {

  private static Logger logger = LoggerFactory.getLogger(FileTransferDemo.class);

  public static void main(String[] args) throws Exception {
    Path dataDir = Paths.get("data");
    Path input = dataDir.resolve("input");
    Path output = dataDir.resolve("output");

    FileTransferDemo demo = new FileTransferDemo();
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

    //Use an anonymous route for the demo
    main.addRouteBuilder(new RouteBuilder() {
      @Override
      public void configure() {
        String in = input.toFile().getAbsolutePath();
        String out = output.toFile().getAbsolutePath();

        //Relative paths can be used too
        //e.g. from("file:data/input?delay=5s")
        //The delay slows down the polling time from the default of 500ms
        from("file:" + in + "?delay=5s")
          .to("file:" + out);
      }
    });

    //This will keep Camel running until you terminate the JVM
    main.run();
  }

}
