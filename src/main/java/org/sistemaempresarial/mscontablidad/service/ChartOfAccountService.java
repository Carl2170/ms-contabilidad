package org.sistemaempresarial.mscontablidad.service;

import org.sistemaempresarial.mscontablidad.entity.ChartOfAccount;
import org.sistemaempresarial.mscontablidad.repository.ChartOfAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChartOfAccountService {

    private final ChartOfAccountRepository chartOfAccountRepository;

    public List<ChartOfAccount> findAll() {
        return chartOfAccountRepository.findAll();
    }

    public Optional<ChartOfAccount> findById(Long id) {
        return chartOfAccountRepository.findById(id);
    }

    public Optional<ChartOfAccount> findDefault() {
        return chartOfAccountRepository.findByIsDefaultTrue();
    }

    @Transactional
    public ChartOfAccount save(ChartOfAccount chartOfAccount) {
        if (Boolean.TRUE.equals(chartOfAccount.getIsDefault())) {
            // If this chart is being set as default, remove default from others
            chartOfAccountRepository.findByIsDefaultTrue()
                    .ifPresent(existing -> {
                        existing.setIsDefault(false);
                        chartOfAccountRepository.save(existing);
                    });
        }
        return chartOfAccountRepository.save(chartOfAccount);
    }

    @Transactional
    public void deleteById(Long id) {
        chartOfAccountRepository.deleteById(id);
    }
}