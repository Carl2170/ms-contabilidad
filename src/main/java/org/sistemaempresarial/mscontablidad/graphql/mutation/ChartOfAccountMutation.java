package org.sistemaempresarial.mscontablidad.graphql.mutation;

import org.sistemaempresarial.mscontablidad.entity.ChartOfAccount;
import org.sistemaempresarial.mscontablidad.service.ChartOfAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChartOfAccountMutation {

    private final ChartOfAccountService chartOfAccountService;

    @MutationMapping
    public ChartOfAccount createChartOfAccount(@Argument ChartOfAccountInput input) {
        ChartOfAccount chartOfAccount = new ChartOfAccount();
        chartOfAccount.setName(input.name());
        chartOfAccount.setDescription(input.description());
        chartOfAccount.setIsDefault(input.isDefault() != null ? input.isDefault() : false);
        return chartOfAccountService.save(chartOfAccount);
    }

    @MutationMapping
    public ChartOfAccount updateChartOfAccount(@Argument Long id, @Argument ChartOfAccountInput input) {
        return chartOfAccountService.findById(id)
                .map(chartOfAccount -> {
                    chartOfAccount.setName(input.name());
                    chartOfAccount.setDescription(input.description());
                    if (input.isDefault() != null) {
                        chartOfAccount.setIsDefault(input.isDefault());
                    }
                    return chartOfAccountService.save(chartOfAccount);
                })
                .orElseThrow(() -> new RuntimeException("Chart of Account not found with id: " + id));
    }

    @MutationMapping
    public Boolean deleteChartOfAccount(@Argument Long id) {
        try {
            chartOfAccountService.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    record ChartOfAccountInput(String name, String description, Boolean isDefault) {}
}
