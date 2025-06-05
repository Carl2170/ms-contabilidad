package org.sistemaempresarial.mscontablidad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounting_period_id", nullable = false)
    private AccountingPeriod accountingPeriod;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    private String description;

    @Column(name = "source_document_id")
    private String sourceDocumentId;

    @Column(name = "source_document_type")
    private String sourceDocumentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JournalEntryStatus status = JournalEntryStatus.DRAFT;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JournalEntryDetail> details;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum JournalEntryStatus {
        DRAFT, POSTED, REVERSED
    }
}