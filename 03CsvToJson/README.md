# Apache Camel - CSV To JSON Demo

This is different to the [Processor](../02Processor/README.md) demo in that it is now converting using Camel `unmarshal` and `marshal` methods.

It expects a CSV in a specific format and is converting it to a JSON file.

The CSV is read (*unmarshal*) by Camel Bindy mapping to a pojo representing the file.

The pojo is then written (*marshal*) to JSON using Jackson.

If you put a file in the wrong format in the `data/input` directory it will be moved to the the `data/error` directory.
This is achieved checking the filename in the route:

    .choice()
    .when(header(Exchange.FILE_NAME).endsWith(".csv"))
      .unmarshal(bindy)
      .marshal().json(JsonLibrary.Jackson)
      .to("file:" + out)
    .otherwise()
      .log(LoggingLevel.ERROR, "Only CSV files accepted. ${file:name} moved to error dir.")
      .to("file:" + err)
      .stop()
    .end();

When the file extension is csv it converts it to Java objects using Bindy then converts it to a JSON String using Jackson.

The `otherwise` block also demonstrates logging in the route and accessing DSl variables.

There are 4 tests covering different aspects:

* `CsvTest` demonstrates unmarshalling a CSV using Camel Bindy. The CSV file is put on the Exchange and converted to a List of Employee objects.
* `JsonTest` demonstrates marshalling objects into JSON Strings using Jackson.
* `CsvToJsonTest` combines the first two by unmarshalling a CSV then marshalling it into JSON.
* `CsvToJsonRouteTest` does the same as the third test but using the production route instead of mock inputs.
