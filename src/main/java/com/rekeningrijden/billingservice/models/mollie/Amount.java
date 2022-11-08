package com.rekeningrijden.billingservice.models.mollie;

import lombok.Data;

@Data
public class Amount {
    private String currency;
    private String value;

    public Amount(String currency, String value) {
        this.currency = currency;
        this.value = value;
    }

    public Amount() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
