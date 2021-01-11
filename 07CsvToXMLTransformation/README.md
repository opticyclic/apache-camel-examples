# Apache Camel - CSV To XML Demo

This combines the [CSV To JSON Demo](03CsvToJson/README.md) and the  [XML Transformation Demo](04XMLTransformation/README.md) to convert
from CSV to XML.

The `CsvToXmlTest` reads the `employee.csv` and converts it to an XML file per row and outputs it to the log.

* The CSV is read (*unmarshal*) by Camel Bindy creating a List of POJOs.
* Camel then *splits* and iterates over each item in the list (row).
* The processor converts the POJO to the JAXB object
* The JAXB object is marshalled to XML
* The XML is logged and also output as a file in the `data` directory
