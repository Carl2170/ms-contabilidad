package org.sistemaempresarial.mscontablidad.graphql.resolver;

import org.sistemaempresarial.mscontablidad.entity.Customer;
import org.sistemaempresarial.mscontablidad.entity.CustomerInvoice;
import org.sistemaempresarial.mscontablidad.entity.CustomerPayment;
import org.sistemaempresarial.mscontablidad.service.CustomerService;
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
public class CustomerResolver {

    private final CustomerService customerService;
    private final CustomerInvoiceService customerInvoiceService;
    private final CustomerPaymentService customerPaymentService;

    @QueryMapping
    public List<Customer> customers() {
        return customerService.findAll();
    }

    @QueryMapping
    public Customer customer(@Argument Long id) {
        return customerService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Customer> activeCustomers() {
        return customerService.findAllActive();
    }

    @QueryMapping
    public List<Customer> searchCustomers(@Argument String searchTerm) {
        return customerService.searchByCodeOrName(searchTerm);
    }

    @SchemaMapping(typeName = "Customer", field = "invoices")
    public List<CustomerInvoice> getInvoices(Customer customer) {
        return customerInvoiceService.findByCustomer(customer);
    }

    @SchemaMapping(typeName = "Customer", field = "payments")
    public List<CustomerPayment> getPayments(Customer customer) {
        return customerPaymentService.findByCustomer(customer);
    }

    @SchemaMapping(typeName = "Customer", field = "totalPendingAmount")
    public String getTotalPendingAmount(Customer customer) {
        BigDecimal amount = customerInvoiceService.getTotalPendingAmountByCustomer(customer.getId());
        return amount.toString();
    }
}