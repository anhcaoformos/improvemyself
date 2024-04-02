package com.produck.service.mapper;

import com.produck.domain.Goal;
import com.produck.domain.Ledger;
import com.produck.service.dto.GoalDTO;
import com.produck.service.dto.LedgerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Goal} and its DTO {@link GoalDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalMapper extends EntityMapper<GoalDTO, Goal> {
    @Mapping(target = "ledger", source = "ledger", qualifiedByName = "ledgerId")
    GoalDTO toDto(Goal s);

    @Named("ledgerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LedgerDTO toDtoLedgerId(Ledger ledger);
}
