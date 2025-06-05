package org.sistemaempresarial.mscontablidad.service;

import org.sistemaempresarial.mscontablidad.entity.JournalEntryDetail;
import org.sistemaempresarial.mscontablidad.entity.JournalEntry;
import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.repository.JournalEntryDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalEntryDetailService {

    private final JournalEntryDetailRepository journalEntryDetailRepository;

    public List<JournalEntryDetail> findAll() {
        return journalEntryDetailRepository.findAll();
    }

    public Optional<JournalEntryDetail> findById(Long id) {
        return journalEntryDetailRepository.findById(id);
    }

    public List<JournalEntryDetail> findByJournalEntry(JournalEntry journalEntry) {
        return journalEntryDetailRepository.findByJournalEntry(journalEntry);
    }

    public List<JournalEntryDetail> findByAccount(AccountingAccount account) {
        return journalEntryDetailRepository.findByAccount(account);
    }

    public List<JournalEntryDetail> findByJournalEntryId(Long journalEntryId) {
        return journalEntryDetailRepository.findByJournalEntryIdOrderById(journalEntryId);
    }

    public BigDecimal getAccountDebitTotal(Long accountId) {
        BigDecimal total = journalEntryDetailRepository.sumDebitAmountByAccountId(accountId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getAccountCreditTotal(Long accountId) {
        BigDecimal total = journalEntryDetailRepository.sumCreditAmountByAccountId(accountId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public JournalEntryDetail save(JournalEntryDetail journalEntryDetail) {
        // Validar que no tenga débito y crédito al mismo tiempo
        if (journalEntryDetail.getDebitAmount().compareTo(BigDecimal.ZERO) > 0 &&
                journalEntryDetail.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("A detail line cannot have both debit and credit amounts");
        }

        return journalEntryDetailRepository.save(journalEntryDetail);
    }

    @Transactional
    public void deleteById(Long id) {
        journalEntryDetailRepository.deleteById(id);
    }
}