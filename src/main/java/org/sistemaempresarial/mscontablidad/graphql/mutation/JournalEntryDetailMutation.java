package org.sistemaempresarial.mscontablidad.graphql.mutation;


import org.sistemaempresarial.mscontablidad.entity.JournalEntryDetail;
import org.sistemaempresarial.mscontablidad.entity.JournalEntry;
import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.service.JournalEntryDetailService;
import org.sistemaempresarial.mscontablidad.service.JournalEntryService;
import org.sistemaempresarial.mscontablidad.service.AccountingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class JournalEntryDetailMutation {

    private final JournalEntryDetailService journalEntryDetailService;
    private final JournalEntryService journalEntryService;
    private final AccountingAccountService accountingAccountService;

    @MutationMapping
    public JournalEntryDetail createJournalEntryDetail(@Argument JournalEntryDetailInput input) {
        JournalEntry journalEntry = journalEntryService.findById(input.journalEntryId())
                .orElseThrow(() -> new RuntimeException("Journal Entry not found with id: " + input.journalEntryId()));

        if (journalEntry.getStatus() == JournalEntry.JournalEntryStatus.POSTED) {
            throw new RuntimeException("Cannot add details to posted journal entries");
        }

        AccountingAccount account = accountingAccountService.findById(input.accountId())
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + input.accountId()));

        JournalEntryDetail detail = new JournalEntryDetail();
        detail.setJournalEntry(journalEntry);
        detail.setAccount(account);
        detail.setDescription(input.description());
        detail.setDebitAmount(new BigDecimal(input.debitAmount()));
        detail.setCreditAmount(new BigDecimal(input.creditAmount()));

        return journalEntryDetailService.save(detail);
    }

    @MutationMapping
    public JournalEntryDetail updateJournalEntryDetail(@Argument Long id, @Argument JournalEntryDetailInput input) {
        return journalEntryDetailService.findById(id)
                .map(detail -> {
                    if (detail.getJournalEntry().getStatus() == JournalEntry.JournalEntryStatus.POSTED) {
                        throw new RuntimeException("Cannot update details of posted journal entries");
                    }

                    if (input.accountId() != null) {
                        AccountingAccount account = accountingAccountService.findById(input.accountId())
                                .orElseThrow(() -> new RuntimeException("Account not found with id: " + input.accountId()));
                        detail.setAccount(account);
                    }

                    detail.setDescription(input.description());
                    detail.setDebitAmount(new BigDecimal(input.debitAmount()));
                    detail.setCreditAmount(new BigDecimal(input.creditAmount()));

                    return journalEntryDetailService.save(detail);
                })
                .orElseThrow(() -> new RuntimeException("Journal Entry Detail not found with id: " + id));
    }

    @MutationMapping
    public Boolean deleteJournalEntryDetail(@Argument Long id) {
        try {
            JournalEntryDetail detail = journalEntryDetailService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Journal Entry Detail not found with id: " + id));

            if (detail.getJournalEntry().getStatus() == JournalEntry.JournalEntryStatus.POSTED) {
                throw new RuntimeException("Cannot delete details from posted journal entries");
            }

            journalEntryDetailService.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    record JournalEntryDetailInput(
            Long journalEntryId,
            Long accountId,
            String description,
            String debitAmount,
            String creditAmount
    ) {}
}