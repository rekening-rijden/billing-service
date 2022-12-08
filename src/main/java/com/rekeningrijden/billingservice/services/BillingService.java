package com.rekeningrijden.billingservice.services;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.payment.PaymentMethod;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.rekeningrijden.billingservice.DistanceCalculator;
import com.rekeningrijden.billingservice.models.CalculatedPrice;
import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.models.DataPoint;
import com.rekeningrijden.billingservice.reporitories.BillingRepository;
import com.rekeningrijden.billingservice.reporitories.InvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BillingService {
    private final BillingRepository billlingRepository;
    private final InvoiceRepository invoiceRepository;
    private final Client mollieClient;
    private final List<PaymentMethod> paymentMethods = List.of(PaymentMethod.IDEAL, PaymentMethod.CREDIT_CARD);
    private final DistanceCalculator distanceCalculator;

    public BillingService(BillingRepository billlingRepository,
                          InvoiceRepository invoiceRepository) {
        this.billlingRepository = billlingRepository;
        this.invoiceRepository = invoiceRepository;
        this.distanceCalculator = new DistanceCalculator();
        this.mollieClient = new ClientBuilder().withApiKey("test_HUS9ADTxAkq5nqAB3RGWxrSaxj55uC").build();

    }

    public CalculatedPrice calculatePrice(List<RouteDTO> routes) {
        double totalDistance = 0;
        double totalVehiclePrice = 0;
        double totalRoadPrice = 0;
        double totalTimePrice = 0;
        double totalPrice = 0;

        for(RouteDTO route: routes) {
            for (int i = 0; i < route.getCoords().size(); i++) {
                if (i + 1 < route.getCoords().size()) {
                    DataPoint dataPoint = route.getCoords().get(i);
                    DataPoint nextDataPoint = route.getCoords().get(i + 1);

                    double distance = distanceCalculator.calculateDistance(
                            dataPoint,
                            nextDataPoint);

                    Date dateTimeDataPoint = dataPoint.getTimestamp();
                    Date dateTimeNextDataPoint = nextDataPoint.getTimestamp();

                    double vehiclePrice = distance * 0.1;
                    double roadPrice = distance * 0.1;
                    double timePrice = distance * 0.1;

                    totalDistance += distance;
                    totalVehiclePrice += vehiclePrice;
                    totalRoadPrice += roadPrice;
                    totalTimePrice += timePrice;
                }
            }
            totalPrice = totalVehiclePrice + totalRoadPrice + totalTimePrice;
        }
        return new CalculatedPrice(
                totalDistance,
                totalVehiclePrice,
                totalRoadPrice,
                totalTimePrice,
                totalPrice);
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
