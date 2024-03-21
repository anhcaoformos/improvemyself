package com.produck.service.mapper;

import com.produck.domain.Ledger;
import com.produck.domain.Objective;
import com.produck.domain.PaymentCategory;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.dto.ObjectiveDTO;
import com.produck.service.dto.PaymentCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Objective} and its DTO {@link ObjectiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface ObjectiveMapper extends EntityMapper<ObjectiveDTO, Objective> {
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "paymentCategoryId")
    @Mapping(target = "ledger", source = "ledger", qualifiedByName = "ledgerId")
    ObjectiveDTO toDto(Objective s);

    @Named("paymentCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentCategoryDTO toDtoPaymentCategoryId(PaymentCategory paymentCategory);

    @Named("ledgerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LedgerDTO toDtoLedgerId(Ledger ledger);
}
