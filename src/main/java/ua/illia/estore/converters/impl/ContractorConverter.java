package ua.illia.estore.converters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.contractor.ContractorResponse;
import ua.illia.estore.model.management.Contractor;
import ua.illia.estore.services.management.PaymentAccountService;

import java.util.stream.Collectors;

@Component
public class ContractorConverter implements EntityConverter<Contractor, ContractorResponse> {

    @Autowired
    private PaymentAccountConverter paymentAccountConverter;

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Override
    public <E extends ContractorResponse> E convert(Contractor contractor, E dto) {
        dto.setId(contractor.getId());
        dto.setName(contractor.getName());
        if (contractor.getPaymentAccounts() != null) {
            dto.setPaymentAccounts(contractor.getPaymentAccounts().stream().map(paymentAccountConverter::paymentAccountResponse).collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    public ContractorResponse convert(Contractor contractor) {
        return convert(contractor, new ContractorResponse());
    }
}
