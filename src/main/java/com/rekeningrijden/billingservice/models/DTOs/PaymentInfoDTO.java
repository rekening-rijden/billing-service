package com.rekeningrijden.billingservice.models.DTOs;

import com.rekeningrijden.billingservice.models.mollie.Address;
import com.rekeningrijden.billingservice.models.mollie.Amount;

public class PaymentInfoDTO {
    private String description;
    private Amount amount;
    private String redirectUrl;
    private String webhookUrl;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;

    public PaymentInfoDTO() {
    }


}