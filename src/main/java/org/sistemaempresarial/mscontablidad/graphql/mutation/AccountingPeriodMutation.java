package org.sistemaempresarial.mscontablidad.graphql.mutation;

import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.sistemaempresarial.mscontablidad.service.AccountingPeriodService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class AccountingPeriodMutation {

    private final AccountingPeriodService accountingPeriodService;

    @MutationMapping
    public AccountingPeriod createAccountingPeriod(@Argument AccountingPeriodInput input) {
        AccountingPeriod accountingPeriod = new AccountingPeriod();
        accountingPeriod.setName(input.name());
        accountingPeriod.setStartDate(LocalDate.parse(input.startDate()));
        accountingPeriod.setEndDate(LocalDate.parse(input.endDate()));
        accountingPeriod.setStatus(input.status());
        return accountingPeriodService.save(accountingPeriod);
    }

    @MutationMapping
    public AccountingPeriod updateAccountingPeriod(@Argument Long id, @Argument AccountingPeriodInput input) {
        return accountingPeriodService.findById(id)
                .map(accountingPeriod -> {
                    accountingPeriod.setName(input.name());
                    accountingPeriod.setStartDate(LocalDate.parse(input.startDate()));
                    accountingPeriod.setEndDate(LocalDate.parse(input.endDate()));
                    accountingPeriod.setStatus(input.status());
                    return accountingPeriodService.save(accountingPeriod);
                })
                .orElseThrow(() -> new RuntimeException("Accounting Period not found with id: " + id));
    }

    @MutationMapping
    public Boolean deleteAccountingPeriod(@Argument Long id) {
        try {
            accountingPeriodService.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    record AccountingPeriodInput(String name, String startDate, String endDate, String status) {}
}