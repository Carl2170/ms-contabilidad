package org.sistemaempresarial.mscontablidad.service;

import org.sistemaempresarial.mscontablidad.entity.AccountingPeriod;
import org.sistemaempresarial.mscontablidad.repository.AccountingPeriodRepository;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountingPeriodService {

    private final AccountingPeriodRepository accountingPeriodRepository;

    public List<AccountingPeriod> findAll() {
        return accountingPeriodRepository.findAll();
    }

    public Optional<AccountingPeriod> findById(Long id) {
        return accountingPeriodRepository.findById(id);
    }

    public List<AccountingPeriod> findByStatus(String status) {
        return accountingPeriodRepository.findByStatus(status);
    }

    @Transactional
    public AccountingPeriod save(AccountingPeriod accountingPeriod) {
        return accountingPeriodRepository.save(accountingPeriod);
    }

    @Transactional
    public void deleteById(Long id) {
        accountingPeriodRepository.deleteById(id);
    }
}