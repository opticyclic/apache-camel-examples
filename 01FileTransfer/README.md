# Apache Camel - File Transfer Demo

This is a simple example of copying files from one directory to another.

Run the main class then put a file in the `data/input` directory.

*NOTE:* The data dir is relative to the working directory which is normally the project root and not the module root.

Camel will copy the file from the `data/input` directory to the `data/output` directory.

Once the copy is complete, the input file is moved to a hidden dir called `.camel` in the input directory.

## Improvements

An anonymous route is used in this demo for simplicity but it could be refactored out to its own class.

The route is essentially doing this:

    from("file:data/input?delay=5s")
      .to("file:data/output");

The `delay=5s` just slows the poll time from the default 0.5s to 5s so the logs in the demo aren't spammed.

A useful extra change is to not process the same file multiple times.
This feature can be added by using the `idempotent` option:

    from("file:data/input?delay=5s&idempotent=true")
      .to("file:data/output");

This defaults to the using the filename as a check. You can customise this check by changing the `idempotentKey`.

e.g. by using the file name and size together:

    from("file:data/input?delay=5s&idempotent=true&idempotentKey=${file:name}-${file:size}")
      .to("file:data/output");
