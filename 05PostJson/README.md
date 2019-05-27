# Apache Camel - POST JSON Demo

This extends the [XML Transformation](../04XMLTransformation/README.md) demo.

It takes the result and POSTs it to a REST API using `camel-http4`.

## Fake REST API

Instead of creating a REST API using Spring Boot (or even [Camel](https://camel.apache.org/rest-dsl.html)), this uses a fake API using [JSON Server](https://github.com/typicode/json-server) for simplicity.

    npm install -g json-server
    json-server --watch db.json --port 3004

When running the tests or the demo the `db.json` file will get updated.

## Things To Note

### Route Definition

The route has been split into two separate routes:

    from("file:" + in + "?delay=5s")
      .choice()
      .when(header(Exchange.FILE_NAME).endsWith(".xml"))
        .unmarshal(jaxb)
        .process(new EmployeeDTOProcessor())
        .marshal().json(JsonLibrary.Jackson)
        .to("direct:post")
      .otherwise()
        .log(LoggingLevel.ERROR, "Only XML files accepted. ${file:name} moved to error dir.")
        .to("file:" + err)
        .stop()
      .end();

    from("direct:post")
      .setHeader(Exchange.HTTP_METHOD, constant(HttpPost.METHOD_NAME))
      .setHeader(Exchange.CONTENT_TYPE, constant(ContentType.APPLICATION_JSON))
      .to("http4://localhost:3004/employees?throwExceptionOnFailure=false")
      .choice()
      .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(201))
        .to("file:" + out)
      .otherwise()
        .log("Error response: ${body}")
        .to("file:" + err)
        .stop()
      .end();

The happy path through the end of the first route ends with `.to("direct:post")`. 
This then starts off the next route `from("direct:post")`.

One benefit of this is that we can test out the POST separate from the transformation.

### HTTP Component

`camel-http` uses the deprecated `Apache HttpClient 3.x.` so we need to use `camel-http4` to use `Apache HttpClient 4.x`.

This has the unfortunate side effect of the url having to start with `http4` instead of `http`.

e.g. `.to("http4://localhost:3004/employees?throwExceptionOnFailure=false")`

The `?throwExceptionOnFailure=false` on the end of the url is an option sent to Camel and not part of the REST API.
This prevents Exceptions being thrown when the REST API returns 400/500 etc.

### Error Handling

Since we have disabled the throwing of an Exception we route depending on the HTTP Response Code.

If we get a 201 (meaning created) we move the file to the out directory.

On any other response we log an error and move to the error directory.
