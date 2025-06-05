package org.sistemaempresarial.mscontablidad.graphql.mutation;


import org.sistemaempresarial.mscontablidad.entity.JournalEntry;
import org.sistemaempresarial.mscontablidad.entity.JournalEntryDetail;
import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.service.JournalEntryService;
import org.sistemaempresarial.mscontablidad.service.AccountingPeriodService;
import org.sistemaempresarial.mscontablidad.service.AccountingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class JournalEntryMutation {

    private final JournalEntryService journalEntryService;
    private final AccountingPeriodService accountingPeriodService;
    private final AccountingAccountService accountingAccountService;

    @MutationMapping
    public JournalEntry createJournalEntry(@Argument JournalEntryInput input) {
        AccountingPeriod accountingPeriod = accountingPeriodService.findById(input.accountingPeriodId())
                .orElseThrow(() -> new RuntimeException("Accounting Period not found with id: " + input.accountingPeriodId()));

        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setAccountingPeriod(accountingPeriod);
        journalEntry.setEntryDate(LocalDate.parse(input.entryDate()));
        journalEntry.setDescription(input.description());
        journalEntry.setSourceDocumentId(input.sourceDocumentId());
        journalEntry.setSourceDocumentType(input.sourceDocumentType());
        journalEntry.setUserId(input.userId());

        // Crear detalles
        List<JournalEntryDetail> details = input.details().stream()
                .map(detailInput -> {
                    AccountingAccount account = accountingAccountService.findById(detailInput.accountId())
                            .orElseThrow(() -> new RuntimeException("Account not found with id: " + detailInput.accountId()));

                    JournalEntryDetail detail = new JournalEntryDetail();
                    detail.setJournalEntry(journalEntry);
                    detail.setAccount(account);
                    detail.setDescription(detailInput.description());
                    detail.setDebitAmount(new BigDecimal(detailInput.debitAmount()));
                    detail.setCreditAmount(new BigDecimal(detailInput.creditAmount()));

                    return detail;
                })
                .collect(Collectors.toList());

        journalEntry.setDetails(details);

        return journalEntryService.save(journalEntry);
    }

    @MutationMapping
    public JournalEntry updateJournalEntry(@Argument Long id, @Argument JournalEntryInput input) {
        return journalEntryService.findById(id)
                .map(journalEntry -> {
                    if (journalEntry.getStatus() == JournalEntry.JournalEntryStatus.POSTED) {
                        throw new RuntimeException("Cannot update posted journal entries");
                    }

                    if (input.accountingPeriodId() != null) {
                        AccountingPeriod accountingPeriod = accountingPeriodService.findById(input.accountingPeriodId())
                                .orElseThrow(() -> new RuntimeException("Accounting Period not found with id: " + input.accountingPeriodId()));
                        journalEntry.setAccountingPeriod(accountingPeriod);
                    }

                    journalEntry.setEntryDate(LocalDate.parse(input.entryDate()));
                    journalEntry.setDescription(input.description());
                    journalEntry.setSourceDocumentId(input.sourceDocumentId());
                    journalEntry.setSourceDocumentType(input.sourceDocumentType());
                    journalEntry.setUserId(input.userId());

                    // Actualizar detalles (eliminar existentes y crear nuevos)
                    journalEntry.getDetails().clear();

                    List<JournalEntryDetail> newDetails = input.details().stream()
                            .map(detailInput -> {
                                AccountingAccount account = accountingAccountService.findById(detailInput.accountId())
                                        .orElseThrow(() -> new RuntimeException("Account not found with id: " + detailInput.accountId()));

                                JournalEntryDetail detail = new JournalEntryDetail();
                                detail.setJournalEntry(journalEntry);
                                detail.setAccount(account);
                                detail.setDescription(detailInput.description());
                                detail.setDebitAmount(new BigDecimal(detailInput.debitAmount()));
                                detail.setCreditAmount(new BigDecimal(detailInput.creditAmount()));

                                return detail;
                            })
                            .collect(Collectors.toList());

                    journalEntry.setDetails(newDetails);

                    return journalEntryService.save(journalEntry);
                })
                .orElseThrow(() -> new RuntimeException("Journal Entry not found with id: " + id));
    }

    @MutationMapping
    public JournalEntry postJournalEntry(@Argument Long id) {
        return journalEntryService.postEntry(id);
    }

    @MutationMapping
    public Boolean deleteJournalEntry(@Argument Long id) {
        try {
            journalEntryService.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Records para inputs
    record JournalEntryInput(
            Long accountingPeriodId,
            String entryDate,
            String description,
            String sourceDocumentId,
            String sourceDocumentType,
            Long userId,
            List<JournalEntryDetailInput> details
    ) {}

    record JournalEntryDetailInput(
            Long accountId,
            String description,
            String debitAmount,
            String creditAmount
    ) {}
}