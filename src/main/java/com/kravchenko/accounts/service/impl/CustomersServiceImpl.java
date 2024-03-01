package com.kravchenko.accounts.service.impl;

import com.kravchenko.accounts.dto.AccountsDto;
import com.kravchenko.accounts.dto.CardsDto;
import com.kravchenko.accounts.dto.CustomerDetailsDto;
import com.kravchenko.accounts.dto.LoansDto;
import com.kravchenko.accounts.entity.Accounts;
import com.kravchenko.accounts.entity.Customer;
import com.kravchenko.accounts.exception.ResourceNotFoundException;
import com.kravchenko.accounts.mapper.AccountsMapper;
import com.kravchenko.accounts.mapper.CustomerMapper;
import com.kravchenko.accounts.repository.AccountsRepository;
import com.kravchenko.accounts.repository.CustomerRepository;
import com.kravchenko.accounts.service.ICustomersService;
import com.kravchenko.accounts.service.client.CardsFeignClient;
import com.kravchenko.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
