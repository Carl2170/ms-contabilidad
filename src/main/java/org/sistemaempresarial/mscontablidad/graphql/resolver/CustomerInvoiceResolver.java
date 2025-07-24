package org.sistemaempresarial.mscontablidad.graphql.resolver;

import org.sistemaempresarial.mscontablidad.entity.CustomerInvoice;
import org.sistemaempresarial.mscontablidad.entity.CustomerInvoiceDetail;
import org.sistemaempresarial.mscontablidad.entity.CustomerPayment;
import org.sistemaempresarial.mscontablidad.service.CustomerInvoiceService;
import org.sistemaempresarial.mscontablidad.service.CustomerPaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerInvoiceResolver {

    private final CustomerInvoiceService customerInvoiceService;
    private final CustomerPaymentService customerPaymentService;

    @QueryMapping
    public List<CustomerInvoice> customerInvoices() {
        return customerInvoiceService.findAll();
    }

    @QueryMapping
    public CustomerInvoice customerInvoice(@Argument Long id) {
        return customerInvoiceService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<CustomerInvoice> customerInvoicesByCustomer(@Argument Long customerId) {
        // Implementar búsqueda por customer ID
        return customerInvoiceService.findAll().stream()
                .filter(invoice -> invoice.getCustomer().getId().equals(customerId))
                .toList();
    }

    @QueryMapping
    public List<CustomerInvoice> customerInvoicesByStatus(@Argument CustomerInvoice.InvoiceStatus status) {
        return customerInvoiceService.findByStatus(status);
    }

    @QueryMapping
    public List<CustomerInvoice> overdueInvoices() {
        return customerInvoiceService.findOverdueInvoices();
    }

    @SchemaMapping(typeName = "CustomerInvoice", field = "paidAmount")
    public String getPaidAmount(CustomerInvoice customerInvoice) {
        BigDecimal amount = customerPaymentService.getTotalPaymentsByInvoice(customerInvoice.getId());
        return amount.toString();
    }

    @SchemaMapping(typeName = "CustomerInvoice", field = "pendingAmount")
    public String getPendingAmount(CustomerInvoice customerInvoice) {
        BigDecimal totalAmount = customerInvoice.getTotalAmount();
        BigDecimal paidAmount = customerPaymentService.getTotalPaymentsByInvoice(customerInvoice.getId());
        return totalAmount.subtract(paidAmount).toString();
    }

    @SchemaMapping(typeName = "CustomerInvoice", field = "payments")
    public List<CustomerPayment> getPayments(CustomerInvoice customerInvoice) {
        return customerPaymentService.findByCustomerInvoice(customerInvoice);
    }
}
