package org.sistemaempresarial.mscontablidad.graphql.resolver;


import org.sistemaempresarial.mscontablidad.entity.JournalEntryDetail;
import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.service.JournalEntryDetailService;
import org.sistemaempresarial.mscontablidad.service.AccountingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class JournalEntryDetailResolver {

    private final JournalEntryDetailService journalEntryDetailService;
    private final AccountingAccountService accountingAccountService;

    @QueryMapping
    public List<JournalEntryDetail> journalEntryDetails() {
        return journalEntryDetailService.findAll();
    }

    @QueryMapping
    public JournalEntryDetail journalEntryDetail(@Argument Long id) {
        return journalEntryDetailService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<JournalEntryDetail> journalEntryDetailsByEntry(@Argument Long journalEntryId) {
        return journalEntryDetailService.findByJournalEntryId(journalEntryId);
    }

    @QueryMapping
    public List<JournalEntryDetail> journalEntryDetailsByAccount(@Argument Long accountId) {
        return accountingAccountService.findById(accountId)
                .map(journalEntryDetailService::findByAccount)
                .orElse(List.of());
    }

    @QueryMapping
    public AccountBalance accountBalance(@Argument Long accountId) {
        AccountingAccount account = accountingAccountService.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        BigDecimal totalDebits = journalEntryDetailService.getAccountDebitTotal(accountId);
        BigDecimal totalCredits = journalEntryDetailService.getAccountCreditTotal(accountId);
        BigDecimal balance = totalDebits.subtract(totalCredits);

        return new AccountBalance(accountId, account, totalDebits, totalCredits, balance);
    }

    // Record para AccountBalance
    public record AccountBalance(
            Long accountId,
            AccountingAccount account,
            BigDecimal totalDebits,
            BigDecimal totalCredits,
            BigDecimal balance
    ) {}
}