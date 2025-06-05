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
public class AccountingAccountResolver {

    private final AccountingAccountService accountingAccountService;
    private final ChartOfAccountService chartOfAccountService;

    @QueryMapping
    public List<AccountingAccount> accountingAccounts() {
        return accountingAccountService.findAll();
    }

    @QueryMapping
    public AccountingAccount accountingAccount(@Argument Long id) {
        return accountingAccountService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<AccountingAccount> accountingAccountsByChartOfAccount(@Argument Long chartOfAccountId) {
        return chartOfAccountService.findById(chartOfAccountId)
                .map(accountingAccountService::findByChartOfAccount)
                .orElse(List.of());
    }

    @QueryMapping
    public List<AccountingAccount> accountingAccountsByParent(@Argument Long parentId) {
        return accountingAccountService.findByParentAccountId(parentId);
    }

    @SchemaMapping(typeName = "AccountingAccount", field = "childAccounts")
    public List<AccountingAccount> getChildAccounts(AccountingAccount account) {
        return accountingAccountService.findByParentAccountId(account.getId());
    }
}