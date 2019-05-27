# Apache Camel - XML Transformation Demo

This builds on the [CSV To JSON](../03CsvToJson/README.md) and [Processor](../02Processor/README.md) demos to process XML, transform the model and convert to JSON.

## JAXB Generation

The JAXB classes are generated from the XSD using a gradle plugin `org.unbroken-dome.xjc` which automatically picks up the XSD from the `src/main/schema` directory.

The generation has been customised in a few ways:

* The `src/main/schema/bindings.xjb` file makes the `Address` a top level class instead of being nested in `Employee`
* The output directory and package have been set in the `build.gradle` so it generates in the main source code
* The `org.jvnet.jaxb2_commons:jaxb2-basics` xjc plugin has been added to generate `toString()` methods
* The `com.github.opticyclic.xml.SimpleToStringStrategy` class is used to clean up the generated `toString()` methods
