package com.produck.service.mapper;

import com.produck.domain.Note;
import com.produck.domain.User;
import com.produck.service.dto.NoteDTO;
import com.produck.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    NoteDTO toDto(Note s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
