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

    public ResponseEntity<?> createPayment(PaymentInfoDTO paymentInfoDTO) {
        // Check if paymetInfoDTO is valid
        if (paymentInfoDTO == null ) {
            return ResponseEntity.badRequest().body("PaymentInfoDTO is null");
        }
        if (paymentInfoDTO.getDescription() == null || paymentInfoDTO.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body("Description is null or empty");
        }
        if (paymentInfoDTO.getAmount() == null || paymentInfoDTO.getAmount().getValue() == null || paymentInfoDTO.getAmount().getValue().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Amount is null");
        }

        // Create payment with mollie
        try {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setAmount(paymentInfoDTO.getAmount());
            paymentRequest.setDescription(paymentInfoDTO.getDescription());
            PaymentResponse paymentResponse = this.mollieClient.payments().createPayment(paymentRequest);
                System.out.println(paymentResponse);
                System.out.println(paymentResponse.getLinks().getCheckout().getHref());
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
