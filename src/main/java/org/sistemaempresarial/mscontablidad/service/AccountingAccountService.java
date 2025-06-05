package org.sistemaempresarial.mscontablidad.service;

import org.sistemaempresarial.mscontablidad.entity.AccountingAccount;
import org.sistemaempresarial.mscontablidad.entity.ChartOfAccount;
import org.sistemaempresarial.mscontablidad.repository.AccountingAccountRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountingAccountService {

    private final AccountingAccountRepository accountingAccountRepository;

    public List<AccountingAccount> findAll() {
        return accountingAccountRepository.findAll();
    }

    public Optional<AccountingAccount> findById(Long id) {
        return accountingAccountRepository.findById(id);
    }

    public List<AccountingAccount> findByChartOfAccount(ChartOfAccount chartOfAccount) {
        return accountingAccountRepository.findByChartOfAccount(chartOfAccount);
    }

    public List<AccountingAccount> findByParentAccountId(Long parentId) {
        return accountingAccountRepository.findByParentAccountId(parentId);
    }

    public Optional<AccountingAccount> findByCodeAndChartOfAccountId(String code, Long chartOfAccountId) {
        return accountingAccountRepository.findByCodeAndChartOfAccountId(code, chartOfAccountId);
    }

    @Transactional
    public AccountingAccount save(AccountingAccount accountingAccount) {
        return accountingAccountRepository.save(accountingAccount);
    }

    @Transactional
    public void deleteById(Long id) {
        accountingAccountRepository.deleteById(id);
    }
}