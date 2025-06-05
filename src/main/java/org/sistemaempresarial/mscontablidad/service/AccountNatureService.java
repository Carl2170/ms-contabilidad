package org.sistemaempresarial.mscontablidad.service;

import org.sistemaempresarial.mscontablidad.entity.AccountNature;
import org.sistemaempresarial.mscontablidad.repository.AccountNatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountNatureService {

    private final AccountNatureRepository accountNatureRepository;

    public List<AccountNature> findAll() {
        return accountNatureRepository.findAll();
    }

    public Optional<AccountNature> findById(Long id) {
        return accountNatureRepository.findById(id);
    }

    @Transactional
    public AccountNature save(AccountNature accountNature) {
        return accountNatureRepository.save(accountNature);
    }

    @Transactional
    public void deleteById(Long id) {
        accountNatureRepository.deleteById(id);
    }
}