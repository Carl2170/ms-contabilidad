package org.sistemaempresarial.mscontablidad.repository;

import org.sistemaempresarial.mscontablidad.entity.CustomerPayment;
import org.sistemaempresarial.mscontablidad.entity.Customer;
import org.sistemaempresarial.mscontablidad.entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

    Optional<CustomerPayment> findByPaymentNumber(String paymentNumber);

    List<CustomerPayment> findByCustomer(Customer customer);

    List<CustomerPayment> findByCustomerInvoice(CustomerInvoice customerInvoice);

    List<CustomerPayment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(cp.amount), 0) FROM CustomerPayment cp WHERE cp.customerInvoice.id = :invoiceId")
    java.math.BigDecimal getTotalPaymentsByInvoice(@Param("invoiceId") Long invoiceId);

    @Query("SELECT cp FROM CustomerPayment cp WHERE cp.customer.id = :customerId ORDER BY cp.paymentDate DESC")
    List<CustomerPayment> findByCustomerIdOrderByPaymentDateDesc(@Param("customerId") Long customerId);
}