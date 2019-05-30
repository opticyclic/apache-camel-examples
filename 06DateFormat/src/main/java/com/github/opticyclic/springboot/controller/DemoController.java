package com.github.opticyclic.springboot.controller;

import java.util.List;

import com.github.opticyclic.springboot.dto.PayStubDTO;
import com.github.opticyclic.springboot.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/demo")
public class DemoController {

  private final PayService service;
  private final static Logger log = LoggerFactory.getLogger(DemoController.class);

  public DemoController(PayService service) {
    this.service = service;
  }

  @GetMapping(value = "/paystubs", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PayStubDTO>> getPayStubs() {
    List<PayStubDTO> payStubs = service.getPayStubs();
    return ResponseEntity.ok(payStubs);
  }

  @PostMapping(value = "/paystubs")
  public PayStubDTO addPayStub(@RequestBody PayStubDTO payStub) {
    log.info("Received " + payStub);
    return service.addPayStub(payStub);
  }
}
