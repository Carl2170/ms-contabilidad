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
public class User {
}
