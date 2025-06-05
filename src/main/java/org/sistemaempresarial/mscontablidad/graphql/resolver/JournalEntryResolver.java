package org.sistemaempresarial.mscontablidad.graphql.resolver;


import org.sistemaempresarial.mscontablidad.entity.JournalEntry;
import org.sistemaempresarial.mscontablidad.entity.JournalEntryDetail;
import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.sistemaempresarial.mscontablidad.service.JournalEntryService;
import org.sistemaempresarial.mscontablidad.service.JournalEntryDetailService;
import org.sistemaempresarial.mscontablidad.service.AccountingPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class JournalEntryResolver {

    private final JournalEntryService journalEntryService;
    private final JournalEntryDetailService journalEntryDetailService;
    private final AccountingPeriodService accountingPeriodService;

    @QueryMapping
    public List<JournalEntry> journalEntries() {
        return journalEntryService.findAll();
    }

    @QueryMapping
    public JournalEntry journalEntry(@Argument Long id) {
        return journalEntryService.findById(id).orElse(null);
    }

    @QueryMapping
    public List<JournalEntry> journalEntriesByPeriod(@Argument Long periodId) {
        return accountingPeriodService.findById(periodId)
                .map(journalEntryService::findByAccountingPeriod)
                .orElse(List.of());
    }

    @QueryMapping
    public List<JournalEntry> journalEntriesByStatus(@Argument JournalEntry.JournalEntryStatus status) {
        return journalEntryService.findByStatus(status);
    }

    @QueryMapping
    public List<JournalEntry> journalEntriesByDateRange(@Argument String startDate, @Argument String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return journalEntryService.findByDateRange(start, end);
    }

    @SchemaMapping(typeName = "JournalEntry", field = "details")
    public List<JournalEntryDetail> getDetails(JournalEntry journalEntry) {
        return journalEntryDetailService.findByJournalEntry(journalEntry);
    }

    @SchemaMapping(typeName = "JournalEntry", field = "totalDebits")
    public String getTotalDebits(JournalEntry journalEntry) {
        BigDecimal total = journalEntry.getDetails().stream()
                .map(JournalEntryDetail::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.toString();
    }

    @SchemaMapping(typeName = "JournalEntry", field = "totalCredits")
    public String getTotalCredits(JournalEntry journalEntry) {
        BigDecimal total = journalEntry.getDetails().stream()
                .map(JournalEntryDetail::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.toString();
    }

    @SchemaMapping(typeName = "JournalEntry", field = "isBalanced")
    public Boolean getIsBalanced(JournalEntry journalEntry) {
        BigDecimal totalDebits = journalEntry.getDetails().stream()
                .map(JournalEntryDetail::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredits = journalEntry.getDetails().stream()
                .map(JournalEntryDetail::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalDebits.compareTo(totalCredits) == 0;
    }
}
