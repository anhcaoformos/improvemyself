package com.produck.service.mapper;

import com.produck.domain.Ledger;
import com.produck.domain.Objective;
import com.produck.domain.PaymentCategory;
import com.produck.domain.PaymentMethod;
import com.produck.domain.Transaction;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.dto.ObjectiveDTO;
import com.produck.service.dto.PaymentCategoryDTO;
import com.produck.service.dto.PaymentMethodDTO;
import com.produck.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "objective", source = "objective", qualifiedByName = "objectiveId")
    @Mapping(target = "paymentMethod", source = "paymentMethod", qualifiedByName = "paymentMethodId")
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "paymentCategoryId")
    @Mapping(target = "ledger", source = "ledger", qualifiedByName = "ledgerId")
    TransactionDTO toDto(Transaction s);

    @Named("objectiveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ObjectiveDTO toDtoObjectiveId(Objective objective);

    @Named("paymentMethodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentMethodDTO toDtoPaymentMethodId(PaymentMethod paymentMethod);

    @Named("paymentCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentCategoryDTO toDtoPaymentCategoryId(PaymentCategory paymentCategory);

    @Named("ledgerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LedgerDTO toDtoLedgerId(Ledger ledger);
}
