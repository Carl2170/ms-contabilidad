package org.sistemaempresarial.mscontablidad.graphql.resolver;

import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.sistemaempresarial.mscontablidad.service.AccountingPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountingPeriodResolver {

    private final AccountingPeriodService accountingPeriodService;

    @QueryMapping
    public List<AccountingPeriod> accountingPeriods() {
        return accountingPeriodService.findAll();
    }

    @QueryMapping
    public AccountingPeriod accountingPeriod(@Argument Long id) {
        return accountingPeriodService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<AccountingPeriod> accountingPeriodsByStatus(@Argument String status) {
        return accountingPeriodService.findByStatus(status);
    }
}