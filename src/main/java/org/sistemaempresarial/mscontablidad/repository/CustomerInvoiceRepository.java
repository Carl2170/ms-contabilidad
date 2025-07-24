package org.sistemaempresarial.mscontablidad.repository;


import org.sistemaempresarial.mscontablidad.entity.CustomerInvoice;
import org.sistemaempresarial.mscontablidad.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {

    Optional<CustomerInvoice> findByInvoiceNumber(String invoiceNumber);

    List<CustomerInvoice> findByCustomer(Customer customer);

    List<CustomerInvoice> findByStatus(CustomerInvoice.InvoiceStatus status);

    List<CustomerInvoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT ci FROM CustomerInvoice ci WHERE ci.customer.id = :customerId AND ci.status = :status")
    List<CustomerInvoice> findByCustomerIdAndStatus(@Param("customerId") Long customerId,
                                                    @Param("status") CustomerInvoice.InvoiceStatus status);

    @Query("SELECT ci FROM CustomerInvoice ci WHERE ci.dueDate < :date AND ci.status = 'PENDING'")
    List<CustomerInvoice> findOverdueInvoices(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(ci.totalAmount), 0) FROM CustomerInvoice ci WHERE ci.customer.id = :customerId AND ci.status = 'PENDING'")
    java.math.BigDecimal getTotalPendingAmountByCustomer(@Param("customerId") Long customerId);
}
