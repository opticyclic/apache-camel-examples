package com.github.opticyclic.springboot.service;

import java.util.ArrayList;
import java.util.List;

import com.github.opticyclic.springboot.dto.PayStubDTO;
import org.springframework.stereotype.Service;

@Service
public class PayService {
  private List<PayStubDTO> payStubs = new ArrayList<>();

  public List<PayStubDTO> getPayStubs() {
    return payStubs;
  }

  public PayStubDTO addPayStub(PayStubDTO payStub) {
    payStubs.add(payStub);
    return payStub;
  }
}
