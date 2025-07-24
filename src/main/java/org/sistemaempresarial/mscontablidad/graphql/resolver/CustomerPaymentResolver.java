package org.sistemaempresarial.mscontablidad.graphql.resolver;


import org.sistemaempresarial.mscontablidad.entity.CustomerPayment;
import org.sistemaempresarial.mscontablidad.service.CustomerPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerPaymentResolver {

    private final CustomerPaymentService customerPaymentService;

    @QueryMapping
    public List<CustomerPayment> customerPayments() {
        return customerPaymentService.findAll();
    }

    @QueryMapping
    public CustomerPayment customerPayment(@Argument Long id) {
        return customerPaymentService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<CustomerPayment> customerPaymentsByCustomer(@Argument Long customerId) {
        return customerPaymentService.findAll().stream()
                .filter(payment -> payment.getCustomer().getId().equals(customerId))
                .toList();
    }

    @QueryMapping
    public List<CustomerPayment> customerPaymentsByInvoice(@Argument Long invoiceId) {
        return customerPaymentService.findAll().stream()
                .filter(payment -> payment.getCustomerInvoice() != null &&
                        payment.getCustomerInvoice().getId().equals(invoiceId))
                .toList();
    }
}
