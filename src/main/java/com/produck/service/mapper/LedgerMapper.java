package com.produck.service.mapper;

import com.produck.domain.ApplicationUser;
import com.produck.domain.Ledger;
import com.produck.service.dto.ApplicationUserDTO;
import com.produck.service.dto.LedgerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ledger} and its DTO {@link LedgerDTO}.
 */
@Mapper(componentModel = "spring")
public interface LedgerMapper extends EntityMapper<LedgerDTO, Ledger> {
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "applicationUserId")
    LedgerDTO toDto(Ledger s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
