# Apache Camel - Processor Demo

This builds on [File Transfer](../01FileTransfer/README.md) demo.

You will notice that the main class is almost identical to the previous demo.

The route has been extracted out to its own class and it now calls a [Camel Processor](https://camel.apache.org/processor.html) on the file before copying it.

The processor in this case is just appending some text and a timestamp to the end of the file.
Therefore, you can put anything in the `data/input` directory (even an empty file) and not worry about formats.

The TextProcessor demonstrates how to get the body of the message, modify and set it back.

It is key to note that the modification is done on the IN message.

Doing `exchange.getOut().setBody(body + newContent)` will create a whole new message and you will lose headers, attachments etc from the IN message.

The filename will also be lost and a generic name based on the message will be used instead.

See [the documentation](https://camel.apache.org/using-getin-or-getout-methods-on-exchange.html) for further explanation.

## Testing

This demo also introduces 2 different tests.

`TextProcessorTest` uses mocks and a `direct` route to isolate the Processor from file activities.

`TextRouteTest` tests the real route. Temporary files are used to keep the tests clean.
