package com.rekeningrijden.billingservice.services;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentMethod;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.reporitories.BillingRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {
    private final BillingRepository billlingRepository;
    private final Client mollieClient;
    private final HttpClient httpClient;
    private final List<PaymentMethod> paymentMethods = List.of(PaymentMethod.IDEAL, PaymentMethod.CREDIT_CARD);

    public BillingService(BillingRepository billlingRepository) {
        this.billlingRepository = billlingRepository;
        this.httpClient = HttpClient.newBuilder().build();
        this.mollieClient = new ClientBuilder().withApiKey("test_HUS9ADTxAkq5nqAB3RGWxrSaxj55uC").build();
    }

    public String test() {
        // return this.billingRepository.findAll();
        return "test";
    }

    public ResponseEntity<?> createPayment(/*PaymentInfoDTO paymentInfoDTO*/) {
        // Check if paymetInfoDTO is valid
//        if (paymentInfoDTO == null) {
//            return ResponseEntity.badRequest().body("PaymentInfoDTO is null");
//        }
        // Create payment with mollie
        try {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setAmount(new Amount("EUR", new BigDecimal("10.00")));
            paymentRequest.setDescription("Test payment");
            paymentRequest.setRedirectUrl(java.util.Optional.of("https://www.google.com"));

            PaymentResponse paymentResponse = this.mollieClient.payments().createPayment(paymentRequest);
                System.out.println(paymentResponse);
            return ResponseEntity.ok(paymentResponse);
        } catch (MollieException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public ResponseEntity<?> getPaymentById(String paymentId) throws MollieException {
        PaymentResponse paymentResponse = this.mollieClient.payments().getPayment(paymentId);
        return null;
    }
}
