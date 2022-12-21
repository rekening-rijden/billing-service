package com.rekeningrijden.billingservice.services;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.Amount;
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
import com.rekeningrijden.billingservice.models.Invoice;
import com.rekeningrijden.billingservice.models.Vehicle;
import com.rekeningrijden.billingservice.reporitories.InvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BillingService {
    private final InvoiceRepository invoiceRepository;
    private final Client mollieClient;

    public BillingService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
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
                    throw new Exception("No tax prices found");
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
        }
        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);
        paymentLink = this.createPaymentLink(
                new PaymentInfoDTO("Invoice for route: " + route.getCoords().get(0).getRouteId(),
                        new Amount(
                                "GBP",
                                totalPrice
                        )
                )
        );
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
            paymentRequest.setRedirectUrl(java.util.Optional.of("http://localhost:4200/thankyou"));
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
        return ResponseEntity.ok(paymentResponse);
    }

    public ResponseEntity<?> getAllInvoices(int carId) {
        List<Invoice> invoices = invoiceRepository.findAllByCarId(carId);
        return ResponseEntity.ok(invoices);
    }

    public Invoice createInvoice(int carId, CalculatedPrice calculatedPrice) {
        Invoice invoice = new Invoice();
        invoice.setCarId(carId);
        invoice.setDate(new Date());
        invoice.setTotalDistance(calculatedPrice.getTotalDistance());
        invoice.setTotalVehiclePrice(calculatedPrice.getTotalVehiclePrice());
        invoice.setTotalRoadPrice(calculatedPrice.getTotalRoadPrice());
        invoice.setTotalTimePrice(calculatedPrice.getTotalTimePrice());
        invoice.setTotalPrice(calculatedPrice.getTotalPrice());
        invoice.setPaymentLink(calculatedPrice.getPaymentLink());
        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceById(int invoiceId) {
        Invoice invoice = this.invoiceRepository.findById(invoiceId).orElse(null);
        return invoice;
    }
}
