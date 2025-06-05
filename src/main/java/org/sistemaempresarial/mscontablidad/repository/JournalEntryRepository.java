package org.sistemaempresarial.mscontablidad.repository;

import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.sistemaempresarial.mscontablidad.entity.JournalEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByAccountingPeriod(AccountingPeriod accountingPeriod);

    List<JournalEntry> findByStatus(JournalEntry.JournalEntryStatus status);

    List<JournalEntry> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT je FROM JournalEntry je WHERE je.accountingPeriod.id = :periodId AND je.status = :status")
    List<JournalEntry> findByPeriodIdAndStatus(@Param("periodId") Long periodId,
                                               @Param("status") JournalEntry.JournalEntryStatus status);

    @Query("SELECT je FROM JournalEntry je WHERE je.sourceDocumentId = :documentId AND je.sourceDocumentType = :documentType")
    List<JournalEntry> findBySourceDocument(@Param("documentId") String documentId,
                                            @Param("documentType") String documentType);
}