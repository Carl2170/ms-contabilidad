package org.sistemaempresarial.mscontablidad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounting_accounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code", "chart_of_account_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_nature_id", nullable = false)
    private AccountNature accountNature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chart_of_account_id", nullable = false)
    private ChartOfAccount chartOfAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private AccountingAccount parentAccount;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer level;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_transactional")
    private Boolean isTransactional = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}