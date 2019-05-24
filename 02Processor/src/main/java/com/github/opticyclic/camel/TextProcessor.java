package com.github.opticyclic.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class TextProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn();
    String body = message.getBody(String.class);
    String newContent = "This is the new content " + System.currentTimeMillis();

    //Note how we are changing the IN message.
    //This doesn't affect the input file and Camel will propagate it correctly to the OUT message
    //Doing exchange.getOut().setBody(body + newContent) will create a whole new message and you will lose headers, attachments etc from the IN message
    message.setBody(body + newContent);
  }
}
