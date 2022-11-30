package com.rekeningrijden.billingservice.models.DTOs;

import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.common.AddressRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Required;

@Getter
@Setter
public class PaymentInfoDTO {
    private String description;
    private Amount amount;
    private String redirectUrl;
    private String webhookUrl;
    private String firstName;
    private String lastName;
    private String email;
    private AddressRequest address;

    // q: how to create constructors for every possible combination of fields?
    public PaymentInfoDTO() {
    }

    public PaymentInfoDTO(String description, Amount amount) {
        this.description = description;
        this.amount = amount;
    }

    public PaymentInfoDTO(String description, Amount amount, String redirectUrl, String webhookUrl, String firstName, String lastName, String email, AddressRequest address) {
        this.description = description;
        this.amount = amount;
        this.redirectUrl = redirectUrl;
        this.webhookUrl = webhookUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

}