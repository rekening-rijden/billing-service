package com.rekeningrijden.billingservice.services;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.reporitories.BillingRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;

@Service
public class BillingService {
    private final BillingRepository billlingRepository;
    private final Client mollieClient;
    private final HttpClient httpClient;

    public BillingService(BillingRepository billlingRepository) {
        this.billlingRepository = billlingRepository;
        this.httpClient = HttpClient.newBuilder().build();
        this.mollieClient = new ClientBuilder().withApiKey(System.getProperty("mollie.api-key")).build();
    }

    public String test() {
        // return this.billingRepository.findAll();
        return "test";
    }

    public ResponseEntity<?> createPayment(PaymentInfoDTO paymentInfoDTO) {
        // Check if paymetInfoDTO is valid

        // Create payment with mollie
        try {
            PaymentRequest request = new PaymentRequest();
            PaymentResponse paymentResponse = this.mollieClient.payments().createPayment()
            return ResponseEntity.ok(paymentResponse);
        } catch (MollieException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public ResponseEntity<?> getPaymentById(String paymentId) throws MollieException {
        PaymentResponse paymentResponse = this.mollieClient.payments().getPayment(paymentId);
    }
}
