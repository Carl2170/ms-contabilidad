package org.sistemaempresarial.mscontablidad.graphql.mutation;


import org.sistemaempresarial.mscontablidad.entity.Customer;
import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.service.CustomerService;
import org.sistemaempresarial.mscontablidad.service.AccountingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CustomerMutation {

    private final CustomerService customerService;
    private final AccountingAccountService accountingAccountService;

    @MutationMapping
    public Customer createCustomer(@Argument CustomerInput input) {
        Customer customer = new Customer();
        customer.setCode(input.code());
        customer.setName(input.name());
        customer.setDocumentType(input.documentType());
        customer.setDocumentNumber(input.documentNumber());
        customer.setEmail(input.email());
        customer.setPhone(input.phone());
        customer.setAddress(input.address());
        customer.setIsActive(input.isActive() != null ? input.isActive() : true);

        if (input.accountingAccountId() != null) {
            AccountingAccount account = accountingAccountService.findById(input.accountingAccountId())
                    .orElseThrow(() -> new RuntimeException("Accounting account not found"));
            customer.setAccountingAccount(account);
        }

        return customerService.save(customer);
    }

    @MutationMapping
    public Customer updateCustomer(@Argument Long id, @Argument CustomerInput input) {
        return customerService.findById(id)
                .map(customer -> {
                    if (input.code() != null) customer.setCode(input.code());
                    customer.setName(input.name());
                    customer.setDocumentType(input.documentType());
                    customer.setDocumentNumber(input.documentNumber());
                    customer.setEmail(input.email());
                    customer.setPhone(input.phone());
                    customer.setAddress(input.address());
                    if (input.isActive() != null) customer.setIsActive(input.isActive());

                    if (input.accountingAccountId() != null) {
                        AccountingAccount account = accountingAccountService.findById(input.accountingAccountId())
                                .orElseThrow(() -> new RuntimeException("Accounting account not found"));
                        customer.setAccountingAccount(account);
                    }

                    return customerService.save(customer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @MutationMapping
    public Boolean deleteCustomer(@Argument Long id) {
        try {
            customerService.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    record CustomerInput(
            String code,
            String name,
            String documentType,
            String documentNumber,
            String email,
            String phone,
            String address,
            Long accountingAccountId,
            Boolean isActive
    ) {}
}
