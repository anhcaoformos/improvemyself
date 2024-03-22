package com.produck.service.mapper;

import com.produck.domain.SplitBook;
import com.produck.domain.User;
import com.produck.service.dto.SplitBookDTO;
import com.produck.service.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link SplitBook} and its DTO {@link SplitBookDTO}.
 */
@Mapper(componentModel = "spring")
public interface SplitBookMapper extends EntityMapper<SplitBookDTO, SplitBook> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    SplitBookDTO toDto(SplitBook s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
