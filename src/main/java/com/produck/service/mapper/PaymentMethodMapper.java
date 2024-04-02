package com.produck.service.mapper;

import com.produck.domain.Ledger;
import com.produck.domain.PaymentMethod;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.dto.PaymentMethodDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentMethod} and its DTO {@link PaymentMethodDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMethodMapper extends EntityMapper<PaymentMethodDTO, PaymentMethod> {
    @Mapping(target = "ledger", source = "ledger", qualifiedByName = "ledgerId")
    PaymentMethodDTO toDto(PaymentMethod s);

    @Named("ledgerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LedgerDTO toDtoLedgerId(Ledger ledger);
}
