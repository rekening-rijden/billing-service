package com.rekeningrijden.billingservice.models;

import com.rekeningrijden.billingservice.models.mollie.Amount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.rekeningrijden.billingservice.models.mollie.Amount;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billingId;
    private int userId;
    private String description;
    private String date;
}
