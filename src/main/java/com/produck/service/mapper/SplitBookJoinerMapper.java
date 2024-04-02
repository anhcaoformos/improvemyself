package com.produck.service.mapper;

import com.produck.domain.SplitBook;
import com.produck.domain.SplitBookJoiner;
import com.produck.service.dto.SplitBookDTO;
import com.produck.service.dto.SplitBookJoinerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SplitBookJoiner} and its DTO {@link SplitBookJoinerDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SplitBookJoinerMapper extends EntityMapper<SplitBookJoinerDTO, SplitBookJoiner> {
    @Mapping(target = "splitBook", source = "splitBook", qualifiedByName = "splitBookId")
    SplitBookJoinerDTO toDto(SplitBookJoiner s);

    @Named("splitBookId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SplitBookDTO toDtoSplitBookId(SplitBook splitBook);
}
