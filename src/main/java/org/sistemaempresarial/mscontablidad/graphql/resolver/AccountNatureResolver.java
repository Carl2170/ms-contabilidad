package org.sistemaempresarial.mscontablidad.graphql.resolver;

import org.sistemaempresarial.mscontablidad.entity.AccountNature;
import org.sistemaempresarial.mscontablidad.service.AccountNatureService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountNatureResolver {

    private final AccountNatureService accountNatureService;

    @QueryMapping
    public List<AccountNature> accountNatures() {
        return accountNatureService.findAll();
    }

    @QueryMapping
    public AccountNature accountNature(@Argument Long id) {
        return accountNatureService.findById(id).orElse(null);
    }
}