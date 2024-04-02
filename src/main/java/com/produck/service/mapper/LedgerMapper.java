package com.produck.service.mapper;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ledger} and its DTO {@link LedgerDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LedgerMapper extends EntityMapper<LedgerDTO, Ledger> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    LedgerDTO toDto(Ledger s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
