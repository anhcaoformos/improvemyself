package com.produck.service.mapper;

import com.produck.domain.SplitBook;
import com.produck.domain.SplitBookDetail;
import com.produck.domain.SplitBookJoiner;
import com.produck.service.dto.SplitBookDTO;
import com.produck.service.dto.SplitBookDetailDTO;
import com.produck.service.dto.SplitBookJoinerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SplitBookDetail} and its DTO {@link SplitBookDetailDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SplitBookDetailMapper extends EntityMapper<SplitBookDetailDTO, SplitBookDetail> {
    @Mapping(target = "splitBookJoiner", source = "splitBookJoiner", qualifiedByName = "splitBookJoinerId")
    @Mapping(target = "splitBook", source = "splitBook", qualifiedByName = "splitBookId")
    SplitBookDetailDTO toDto(SplitBookDetail s);

    @Named("splitBookJoinerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SplitBookJoinerDTO toDtoSplitBookJoinerId(SplitBookJoiner splitBookJoiner);

    @Named("splitBookId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SplitBookDTO toDtoSplitBookId(SplitBook splitBook);
}
