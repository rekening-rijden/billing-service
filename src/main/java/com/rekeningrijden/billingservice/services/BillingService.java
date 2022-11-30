package com.rekeningrijden.billingservice.services;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentMethod;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.models.Invoice;
import com.rekeningrijden.billingservice.reporitories.BillingRepository;
import com.rekeningrijden.billingservice.reporitories.InvoiceRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillingService {
    private final BillingRepository billlingRepository;
    private final InvoiceRepository invoiceRepository;
    private final Client mollieClient;
    private final HttpClient httpClient;
    private final List<PaymentMethod> paymentMethods = List.of(PaymentMethod.IDEAL, PaymentMethod.CREDIT_CARD);

    public BillingService(BillingRepository billlingRepository, InvoiceRepository invoiceRepository) {
        this.billlingRepository = billlingRepository;
        this.invoiceRepository = invoiceRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.mollieClient = new ClientBuilder().withApiKey("test_HUS9ADTxAkq5nqAB3RGWxrSaxj55uC").build();
    }

    public String test() {
        // return this.billingRepository.findAll();
        return "test";
    }

    public ResponseEntity<?> createPaymentLink(PaymentInfoDTO paymentInfoDTO) {
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
            paymentRequest.setRedirectUrl(java.util.Optional.of("https://www.google.com/"));
            PaymentResponse paymentResponse = this.mollieClient.payments().createPayment(paymentRequest);
                System.out.println(paymentResponse);
                System.out.println(paymentResponse.getLinks().getCheckout().getHref());
            return ResponseEntity.ok(paymentResponse.getLinks().getCheckout().getHref());
        } catch (MollieException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public ResponseEntity<?> getPaymentById(String paymentId) throws MollieException {
        PaymentResponse paymentResponse = this.mollieClient.payments().getPayment(paymentId);
        return null;
    }

    public ResponseEntity<?> getAllInvoices(String carId) {
        return invoiceRepository.findAllByCarId(carId);
    }

//    public ResponseEntity<?> createInvoiceByRoutes(List<RouteDTO> routes) {
//        // Check if routes is valid
//        if (routes == null || routes.isEmpty()) {
//            return ResponseEntity.badRequest().body("Routes is null or empty");
//        }
//
//        // Get price per kilometer
//        var pricePerKilometer = "0.15";
//
//        // Create invoice
//        var invoice = new Invoice();
//        invoice.setCarId(routes.get(0).getCarId());
//        invoice.setPricePerKilometer(new BigDecimal(pricePerKilometer));
//        invoice.setRoutes(routes);
//        invoice.setTotalPrice(calculateTotalPrice(routes, new BigDecimal(pricePerKilometer)));
//        invoice.setPaid(false);
//        invoice.setPaymentId(null);
//
//
//        invoiceRepository.save(invoice);
//        return ResponseEntity.ok(invoice);
//    }
//
//    public ResponseEntity<?> createInvoiceByCarId(String carId) {
//        // Check if carId is valid
//        if (carId == null || carId.isEmpty()) {
//            return ResponseEntity.badRequest().body("CarId is null or empty");
//        }
//
//        // Get Car information.
//        var carInfo = getCarInfo(carId);
//        if (carInfo == null) {
//            return ResponseEntity.badRequest().body("CarId is not valid");
//        }
//    }
}
