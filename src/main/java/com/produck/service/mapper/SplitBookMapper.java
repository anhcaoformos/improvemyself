package com.produck.service.mapper;

import com.produck.domain.ApplicationUser;
import com.produck.domain.SplitBook;
import com.produck.service.dto.ApplicationUserDTO;
import com.produck.service.dto.SplitBookDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SplitBook} and its DTO {@link SplitBookDTO}.
 */
@Mapper(componentModel = "spring")
public interface SplitBookMapper extends EntityMapper<SplitBookDTO, SplitBook> {
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "applicationUserId")
    SplitBookDTO toDto(SplitBook s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
