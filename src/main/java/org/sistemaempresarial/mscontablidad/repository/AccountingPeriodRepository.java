package org.sistemaempresarial.mscontablidad.repository;

import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountingPeriodRepository extends JpaRepository<AccountingPeriod, Long> {
    List<AccountingPeriod> findByStatus(String status);
}
