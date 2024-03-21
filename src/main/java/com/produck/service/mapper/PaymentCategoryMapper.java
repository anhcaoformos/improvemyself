package com.produck.service.mapper;

import com.produck.domain.Ledger;
import com.produck.domain.PaymentCategory;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.dto.PaymentCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCategory} and its DTO {@link PaymentCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentCategoryMapper extends EntityMapper<PaymentCategoryDTO, PaymentCategory> {
    @Mapping(target = "ledger", source = "ledger", qualifiedByName = "ledgerId")
    PaymentCategoryDTO toDto(PaymentCategory s);

    @Named("ledgerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LedgerDTO toDtoLedgerId(Ledger ledger);
}
