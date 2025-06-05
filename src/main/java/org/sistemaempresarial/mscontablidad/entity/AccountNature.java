package org.sistemaempresarial.mscontablidad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_natures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountNature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "default_balance_type", nullable = false)
    private String defaultBalanceType;
}
