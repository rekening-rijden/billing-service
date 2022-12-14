package com.rekeningrijden.billingservice.services;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.payment.PaymentMethod;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.exception.MollieException;
import com.rekeningrijden.billingservice.DistanceCalculator;
import com.rekeningrijden.billingservice.models.CalculatedPrice;
import com.rekeningrijden.billingservice.models.DTOs.PaymentInfoDTO;
import com.rekeningrijden.billingservice.models.DTOs.RouteDTO;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.BasePriceDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.RoadTaxDto;
import com.rekeningrijden.billingservice.models.DTOs.TaxConfig.TimeTaxDto;
import com.rekeningrijden.billingservice.models.DataPoint;
import com.rekeningrijden.billingservice.models.Vehicle;
import com.rekeningrijden.billingservice.reporitories.BillingRepository;
import com.rekeningrijden.billingservice.reporitories.InvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BillingService {
    private final BillingRepository billlingRepository;
    private final InvoiceRepository invoiceRepository;
    private final Client mollieClient;
    private final HttpClient httpClient;

    public BillingService(BillingRepository billlingRepository, InvoiceRepository invoiceRepository) {
        this.billlingRepository = billlingRepository;
        this.invoiceRepository = invoiceRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.mollieClient = new ClientBuilder().withApiKey("test_HUS9ADTxAkq5nqAB3RGWxrSaxj55uC").build();
    }


    public CalculatedPrice calculatePrice(RouteDTO route, List<BasePriceDto> basePrices, List<RoadTaxDto> roadTaxes, List<TimeTaxDto> timeTaxes, Vehicle vehicle) throws Exception {
        BigDecimal totalDistance = BigDecimal.valueOf(0);
        BigDecimal totalVehiclePrice = BigDecimal.valueOf(0);
        BigDecimal totalRoadPrice = BigDecimal.valueOf(0);
        BigDecimal totalTimePrice = BigDecimal.valueOf(0);
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        BigDecimal vehicleTaxPrice;
        BigDecimal roadTaxPrice;
        BigDecimal timeTaxPrice;

        // set vehicleTaxPrice to the value of the vehicle from the basePrices list
        vehicleTaxPrice = basePrices.stream()
                .filter(basePrice -> basePrice.getEngineType().equals(vehicle.getFuelType()))
                .findFirst()
                .map(BasePriceDto::getSurTax)
                .orElse(null);

        String paymentLink = null;
        for (int i = 0; i < route.getCoords().size(); i++) {
            if (i + 1 < route.getCoords().size()) {
                DataPoint dataPoint = route.getCoords().get(i);
                DataPoint nextDataPoint = route.getCoords().get(i + 1);

                BigDecimal distance = BigDecimal.valueOf(DistanceCalculator.haversineDistance(
                        dataPoint.getLat(),
                        dataPoint.getLng(),
                        nextDataPoint.getLat(),
                        nextDataPoint.getLng()
                ));

                Calendar calendarDataPoint = Calendar.getInstance();
                calendarDataPoint.setTime(dataPoint.getTimestamp());
                int dayOfWeek = calendarDataPoint.get(Calendar.DAY_OF_WEEK);

                Calendar calendarNextDataPoint = Calendar.getInstance();
                calendarNextDataPoint.setTime(nextDataPoint.getTimestamp());


                timeTaxPrice = timeTaxes.stream()
                        .filter(timeTax -> timeTax.getDayOfWeek() == dayOfWeek)
                        .filter(timeTax -> timeTax.getStartTime().getHour() == calendarDataPoint.get(Calendar.HOUR_OF_DAY))
                        .map(TimeTaxDto::getSurTax)
                        .findFirst()
                        .orElse(null);

                roadTaxPrice = roadTaxes.stream()
                        .filter(roadTax -> roadTax.getRoadType().equals(dataPoint.getRoadType()))
                        .map(RoadTaxDto::getSurTax)
                        .findFirst()
                        .orElse(null);

                if (timeTaxPrice == null || roadTaxPrice == null || vehicleTaxPrice == null) {
                    throw new RuntimeException("No tax prices found");
                }
                BigDecimal vehiclePrice = distance.multiply(vehicleTaxPrice);
                BigDecimal roadPrice = distance.multiply(roadTaxPrice);
                BigDecimal timePrice = distance.multiply(timeTaxPrice);

                totalDistance = totalDistance.add(distance);
                totalVehiclePrice = totalVehiclePrice.add(vehiclePrice);
                totalRoadPrice = totalRoadPrice.add(roadPrice);
                totalTimePrice = totalTimePrice.add(timePrice);
            }


            totalPrice = totalVehiclePrice.add(totalRoadPrice).add(totalTimePrice);
            paymentLink = this.createPaymentLink(
                    new PaymentInfoDTO("Invoice for route: " + route.getCoords().get(0).getRouteId(),
                            new Amount(
                                    "GBP",
                                    totalPrice
                            )
                    )
            );
        }
        return new CalculatedPrice(
                totalDistance,
                totalVehiclePrice,
                totalRoadPrice,
                totalTimePrice,
                totalPrice,
                paymentLink
        );
    }

    public String createPaymentLink(PaymentInfoDTO paymentInfoDTO) throws Exception {
        // Check if paymetInfoDTO is valid
        if (paymentInfoDTO == null ) {
            throw new Exception("PaymentInfoDTO is null");
        }
        if (paymentInfoDTO.getDescription() == null || paymentInfoDTO.getDescription().isEmpty()) {
            throw new Exception("Description is null or empty");
        }
        if (paymentInfoDTO.getAmount() == null || paymentInfoDTO.getAmount().getValue() == null || paymentInfoDTO.getAmount().getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Amount is null");
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
            return paymentResponse.getLinks().getCheckout().getHref();
        } catch (MollieException e) {
            e.printStackTrace();
            return e.getMessage();
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
