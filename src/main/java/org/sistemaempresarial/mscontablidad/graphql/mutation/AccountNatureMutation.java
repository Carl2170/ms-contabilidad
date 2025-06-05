package org.sistemaempresarial.mscontablidad.graphql.mutation;

import org.sistemaempresarial.mscontablidad.entity.AccountNature;
import org.sistemaempresarial.mscontablidad.service.AccountNatureService;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AccountNatureMutation {

    private final AccountNatureService accountNatureService;

    @MutationMapping
    public AccountNature createAccountNature(@Argument AccountNatureInput input) {
        AccountNature accountNature = new AccountNature();
        accountNature.setName(input.name()); // Usar input.name() en lugar de input.getName()
        accountNature.setDefaultBalanceType(input.defaultBalanceType()); // Usar input.defaultBalanceType()
        return accountNatureService.save(accountNature);
    }

    @MutationMapping
    public AccountNature updateAccountNature(@Argument Long id, @Argument AccountNatureInput input) {
        return accountNatureService.findById(id)
                .map(accountNature -> {
                    accountNature.setName(input.name());
                    accountNature.setDefaultBalanceType(input.defaultBalanceType());
                    return accountNatureService.save(accountNature);
                })
                .orElseThrow(() -> new RuntimeException("Account Nature not found with id: " + id));
    }

    @MutationMapping
    public Boolean deleteAccountNature(@Argument Long id) {
        try {
            accountNatureService.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    record AccountNatureInput(String name, String defaultBalanceType) {}
}