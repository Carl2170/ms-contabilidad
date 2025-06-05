package org.sistemaempresarial.mscontablidad.graphql.mutation;

import org.sistemaempresarial.mscontablidad.entity.AccountNature;
import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.entity.ChartOfAccount;
import org.sistemaempresarial.mscontablidad.service.AccountNatureService;
import org.sistemaempresarial.mscontablidad.service.AccountingAccountService;
import org.sistemaempresarial.mscontablidad.service.ChartOfAccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AccountingAccountMutation {

    private final AccountingAccountService accountingAccountService;
    private final AccountNatureService accountNatureService;
    private final ChartOfAccountService chartOfAccountService;

    @MutationMapping
    public AccountingAccount createAccountingAccount(@Argument AccountingAccountInput input) {
        AccountNature accountNature = accountNatureService.findById(input.accountNatureId())
                .orElseThrow(() -> new RuntimeException("Account Nature not found with id: " + input.accountNatureId()));

        ChartOfAccount chartOfAccount = chartOfAccountService.findById(input.chartOfAccountId())
                .orElseThrow(() -> new RuntimeException("Chart of Account not found with id: " + input.chartOfAccountId()));

        AccountingAccount parentAccount = null;
        if (input.parentAccountId() != null) {
            parentAccount = accountingAccountService.findById(input.parentAccountId())
                    .orElseThrow(() -> new RuntimeException("Parent Account not found with id: " + input.parentAccountId()));
        }

        AccountingAccount accountingAccount = new AccountingAccount();
        accountingAccount.setAccountNature(accountNature);
        accountingAccount.setChartOfAccount(chartOfAccount);
        accountingAccount.setParentAccount(parentAccount);
        accountingAccount.setCode(input.code());
        accountingAccount.setName(input.name());
        accountingAccount.setDescription(input.description());
        accountingAccount.setLevel(input.level());
        accountingAccount.setIsActive(input.isActive() != null ? input.isActive() : true);
        accountingAccount.setIsTransactional(input.isTransactional() != null ? input.isTransactional() : true);

        return accountingAccountService.save(accountingAccount);
    }

    @MutationMapping
    public AccountingAccount updateAccountingAccount(@Argument Long id, @Argument AccountingAccountInput input) {
        return accountingAccountService.findById(id)
                .map(accountingAccount -> {
                    if (input.accountNatureId() != null) {
                        AccountNature accountNature = accountNatureService.findById(input.accountNatureId())
                                .orElseThrow(() -> new RuntimeException("Account Nature not found with id: " + input.accountNatureId()));
                        accountingAccount.setAccountNature(accountNature);
                    }

                    if (input.chartOfAccountId() != null) {
                        ChartOfAccount chartOfAccount = chartOfAccountService.findById(input.chartOfAccountId())
                                .orElseThrow(() -> new RuntimeException("Chart of Account not found with id: " + input.chartOfAccountId()));
                        accountingAccount.setChartOfAccount(chartOfAccount);
                    }

                    if (input.parentAccountId() != null) {
                        AccountingAccount parentAccount = accountingAccountService.findById(input.parentAccountId())
                                .orElseThrow(() -> new RuntimeException("Parent Account not found with id: " + input.parentAccountId()));
                        accountingAccount.setParentAccount(parentAccount);
                    }

                    accountingAccount.setCode(input.code());
                    accountingAccount.setName(input.name());
                    accountingAccount.setDescription(input.description());
                    accountingAccount.setLevel(input.level());

                    if (input.isActive() != null) {
                        accountingAccount.setIsActive(input.isActive());
                    }

                    if (input.isTransactional() != null) {
                        accountingAccount.setIsTransactional(input.isTransactional());
                    }

                    return accountingAccountService.save(accountingAccount);
                })
                .orElseThrow(() -> new RuntimeException("Accounting Account not found with id: " + id));
    }

    @MutationMapping
    public Boolean deleteAccountingAccount(@Argument Long id) {
        try {
            accountingAccountService.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    record AccountingAccountInput(
            Long accountNatureId,
            Long chartOfAccountId,
            Long parentAccountId,
            String code,
            String name,
            String description,
            Integer level,
            Boolean isActive,
            Boolean isTransactional) {}
}
