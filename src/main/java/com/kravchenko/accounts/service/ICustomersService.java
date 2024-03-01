package com.kravchenko.accounts.service;

import com.kravchenko.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {

    /**
     * @param mobileNumber  - Input Mobile Number
     * @param correlationId - CorrelationId
     * @return Customer Details based on a given mobileNumber
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
