package org.sistemaempresarial.mscontablidad.service;


import org.sistemaempresarial.mscontablidad.entity.JournalEntry;
import org.sistemaempresarial.mscontablidad.entity.JournalEntryDetail;
import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.sistemaempresarial.mscontablidad.repository.JournalEntryRepository;
import org.sistemaempresarial.mscontablidad.repository.JournalEntryDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalEntryDetailRepository journalEntryDetailRepository;

    public List<JournalEntry> findAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(Long id) {
        return journalEntryRepository.findById(id);
    }

    public List<JournalEntry> findByAccountingPeriod(AccountingPeriod accountingPeriod) {
        return journalEntryRepository.findByAccountingPeriod(accountingPeriod);
    }

    public List<JournalEntry> findByStatus(JournalEntry.JournalEntryStatus status) {
        return journalEntryRepository.findByStatus(status);
    }

    public List<JournalEntry> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return journalEntryRepository.findByEntryDateBetween(startDate, endDate);
    }

    public List<JournalEntry> findByPeriodIdAndStatus(Long periodId, JournalEntry.JournalEntryStatus status) {
        return journalEntryRepository.findByPeriodIdAndStatus(periodId, status);
    }

    @Transactional
    public JournalEntry save(JournalEntry journalEntry) {
        // Validar que el asiento esté balanceado antes de guardar
        if (journalEntry.getDetails() != null && !journalEntry.getDetails().isEmpty()) {
            validateBalancedEntry(journalEntry);
        }
        return journalEntryRepository.save(journalEntry);
    }

    @Transactional
    public JournalEntry postEntry(Long id) {
        return journalEntryRepository.findById(id)
                .map(entry -> {
                    if (entry.getStatus() != JournalEntry.JournalEntryStatus.DRAFT) {
                        throw new RuntimeException("Only DRAFT entries can be posted");
                    }

                    // Validar que esté balanceado
                    validateBalancedEntry(entry);

                    entry.setStatus(JournalEntry.JournalEntryStatus.POSTED);
                    entry.setPostedAt(LocalDateTime.now());
                    return journalEntryRepository.save(entry);
                })
                .orElseThrow(() -> new RuntimeException("Journal Entry not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);
        if (entry.isPresent() && entry.get().getStatus() == JournalEntry.JournalEntryStatus.POSTED) {
            throw new RuntimeException("Cannot delete posted journal entries");
        }
        journalEntryRepository.deleteById(id);
    }

    private void validateBalancedEntry(JournalEntry journalEntry) {
        BigDecimal totalDebits = journalEntry.getDetails().stream()
                .map(JournalEntryDetail::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredits = journalEntry.getDetails().stream()
                .map(JournalEntryDetail::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new RuntimeException("Journal entry is not balanced. Debits: " + totalDebits + ", Credits: " + totalCredits);
        }
    }
}