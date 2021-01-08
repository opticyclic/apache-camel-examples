package com.github.opticyclic.camel.processors;

import com.github.opticyclic.camel.Employee;
import com.github.opticyclic.camel.model.Address;
import com.github.opticyclic.camel.model.XmlEmployee;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class ProcessEmployee implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn();
    Employee body = message.getBody(Employee.class);

    XmlEmployee xmlEmployee = getXmlEmployee(body);
    message.setBody(xmlEmployee);
  }

  public XmlEmployee getXmlEmployee(Employee employee) {
    XmlEmployee xmlEmployee = new XmlEmployee();
    xmlEmployee.setFirstName(employee.getFirstName());
    xmlEmployee.setLastName(employee.getLastName());
    Address address = new Address();
    address.setAddressLine1(employee.getAddressLine1());
    address.setAddressLine2(employee.getAddressLine2());
    address.setCity(employee.getCity());
    address.setState(employee.getState());
    address.setPostalCode(employee.getPostalCode());
    address.setCountry(employee.getCountry());
    xmlEmployee.setAddress(address);
    xmlEmployee.setPosition(employee.getPosition());
    return xmlEmployee;
  }
}