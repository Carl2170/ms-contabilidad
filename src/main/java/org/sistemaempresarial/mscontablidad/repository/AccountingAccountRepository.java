package org.sistemaempresarial.mscontablidad.repository;

import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.entity.ChartOfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountingAccountRepository extends JpaRepository<AccountingAccount, Long> {
    List<AccountingAccount> findByChartOfAccount(ChartOfAccount chartOfAccount);
    List<AccountingAccount> findByParentAccountId(Long parentId);
    Optional<AccountingAccount> findByCodeAndChartOfAccountId(String code, Long chartOfAccountId);
}