package com.github.opticyclic.camel;

import com.github.opticyclic.camel.model.Employee;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * This class converts from the JAXB Employee to the EmployeeDTO.
 * The models aren't great examples but are just to demonstrate a conversion.
 */
public class EmployeeDTOProcessor implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn();
    Employee employee = message.getBody(Employee.class);

    EmployeeDTO employeeDTO = new EmployeeDTO();
    employeeDTO.setFirstName(employee.getFirstName());
    employeeDTO.setLastName(employee.getLastName());
    employeeDTO.setPosition(employee.getPosition());

    message.setBody(employeeDTO);
  }
}
