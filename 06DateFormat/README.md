# Apache Camel - Date Format Demo

This demonstrates how to use `LocalDate` in a DTO and serialise it back and forth from JSON.

A simple Spring Boot REST API is used (instead of the JSON server previously) to verify the deserialization.

In order to change the date format we need to modify the Jackson ObjectMapper:

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

We then create our own JacksonDataFormat in the route.

    DataFormat jackson = new JacksonDataFormat(objectMapper, PayStubDTO.class);

    from("direct:start")
      .marshal(jackson)
      .to("direct:post");

Notice how this is different from previously where we used the `json` helper method to create the DataFormat:
:

    .marshal().json(JsonLibrary.Jackson)

Rather than using a file input like the previous examples, this users the `timer` component to send a message every 5 seconds.

    from("timer:foo?period=5s")

**NOTE:** The Spring Boot application also needs to have the same ObjectMapper set:


    @SpringBootApplication
    public class DemoRestApplication {
    
      public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoRestApplication.class);
        app.run(args);
      }
    
      @Bean
      @Primary
      public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    
        return objectMapper;
      }
    }


## Running

Run the Spring boot application `DemoRestApplication` either from the IDE or using `./gradlew bootRun`

This will create a REST API at [http://localhost:8080/api/demo/paystubs]

Run the `DateFormatDemo` and you will see a message in the Spring Boot logs every 5 seconds.
 