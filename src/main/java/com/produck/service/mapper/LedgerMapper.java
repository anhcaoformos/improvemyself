package com.produck.service.mapper;

import com.produck.domain.Ledger;
import com.produck.domain.User;
import com.produck.service.dto.LedgerDTO;
import com.produck.service.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Ledger} and its DTO {@link LedgerDTO}.
 */
@Mapper(componentModel = "spring")
public interface LedgerMapper extends EntityMapper<LedgerDTO, Ledger> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    LedgerDTO toDto(Ledger s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
