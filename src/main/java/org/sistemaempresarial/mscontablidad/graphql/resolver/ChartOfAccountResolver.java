package org.sistemaempresarial.mscontablidad.graphql.resolver;


import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.entity.ChartOfAccount;
import org.sistemaempresarial.mscontablidad.service.AccountingAccountService;
import org.sistemaempresarial.mscontablidad.service.ChartOfAccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartOfAccountResolver {

    private final ChartOfAccountService chartOfAccountService;
    private final AccountingAccountService accountingAccountService;

    @QueryMapping
    public List<ChartOfAccount> chartOfAccounts() {
        return chartOfAccountService.findAll();
    }

    @QueryMapping
    public ChartOfAccount chartOfAccount(@Argument Long id) {
        return chartOfAccountService.findById(id).orElse(null);
    }

    @QueryMapping
    public ChartOfAccount defaultChartOfAccount() {
        return chartOfAccountService.findDefault().orElse(null);
    }

    @SchemaMapping(typeName = "ChartOfAccount", field = "accounts")
    public List<AccountingAccount> getAccounts(ChartOfAccount chartOfAccount) {
        return accountingAccountService.findByChartOfAccount(chartOfAccount);
    }
}