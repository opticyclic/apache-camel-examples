# Apache Camel Examples

There are plenty of Apache Camel
[examples in the main repo](https://github.com/apache/camel/tree/master/examples).

However, it is a big repo to clone and has a lot of examples that are probably not relevant and it isn't structured well.

e.g. there is a mix of Spring XML and JavaConfig and several different ways to start an application without a clear progression on which is the recommended way. 

This project aims to extract out some of the more basic examples as an introduction to how Camel works.

This project will use gradle instead of maven that the main Camel repo uses.

## Demos

The demos gradually build on each other getting more complex.

* [File Transfer Demo](01FileTransfer/README.md) - Copies files from one directory to another
* [Processor Demo](02Processor/README.md) - Adds a processor to modify the message along the way
* [CSV To JSON Demo](03CsvToJson/README.md) - Converts from CSV to JSON
* [XML Transformation Demo](04XMLTransformation/README.md) - Reads an XML file with JAXB, transforms to a new model and converts to JSON
* [POST JSON Demo](05PostJson/README.md) - Same as XML Transformation but POSTs the response to a REST API
* [Date Format Demo](06DateFormat/README.md) - Shows how to modify date formats using Jackson
* [CSV To XML Demo](07CsvToXMLTransformation/README.md) - Combines previous examples to convert from CSV to XML using Camel Bindy
