package com.kravchenko.accounts.service.client;

import com.kravchenko.accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans", fallback = LoansFallback.class)
public interface LoansFeignClient {

    @GetMapping("/api/fetch")
    ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("eazybank-correlation-id") String correlationId,
                                              @RequestParam String mobileNumber);

}
