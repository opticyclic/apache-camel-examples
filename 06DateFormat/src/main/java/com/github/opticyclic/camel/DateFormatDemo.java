package com.github.opticyclic.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

/**
 * This is demo posts a message every 5 seconds to a REST API
 */
public class DateFormatDemo {

  public static void main(String[] args) throws Exception {
    DateFormatDemo demo = new DateFormatDemo();
    demo.boot();
  }

  private void boot() throws Exception {
    //Use the Camel Main instance instead of manually creating a CamelContext
    Main main = new Main();

    //Add a route to process the input files
    RouteBuilder routeBuilder = new DateFormatRoute("5s");
    main.addRouteBuilder(routeBuilder);

    //This will keep Camel running until you terminate the JVM
    main.run();
  }

}
