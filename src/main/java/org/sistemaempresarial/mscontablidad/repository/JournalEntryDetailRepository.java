package org.sistemaempresarial.mscontablidad.repository;

import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.entity.JournalEntry;
import org.sistemaempresarial.mscontablidad.entity.JournalEntryDetail;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JournalEntryDetailRepository extends JpaRepository<JournalEntryDetail, Long> {

    List<JournalEntryDetail> findByJournalEntry(JournalEntry journalEntry);

    List<JournalEntryDetail> findByAccount(AccountingAccount account);

    @Query("SELECT SUM(jed.debitAmount) FROM JournalEntryDetail jed WHERE jed.account.id = :accountId")
    BigDecimal sumDebitAmountByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT SUM(jed.creditAmount) FROM JournalEntryDetail jed WHERE jed.account.id = :accountId")
    BigDecimal sumCreditAmountByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT jed FROM JournalEntryDetail jed WHERE jed.journalEntry.id = :journalEntryId ORDER BY jed.id")
    List<JournalEntryDetail> findByJournalEntryIdOrderById(@Param("journalEntryId") Long journalEntryId);
}