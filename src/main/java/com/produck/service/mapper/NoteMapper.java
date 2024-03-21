package com.produck.service.mapper;

import com.produck.domain.ApplicationUser;
import com.produck.domain.Note;
import com.produck.service.dto.ApplicationUserDTO;
import com.produck.service.dto.NoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "applicationUserId")
    NoteDTO toDto(Note s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
