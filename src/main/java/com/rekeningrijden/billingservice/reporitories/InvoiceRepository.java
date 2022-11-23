package com.rekeningrijden.billingservice.reporitories;

import com.rekeningrijden.billingservice.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    ResponseEntity<?> findAllByCarId(String carId);
}
