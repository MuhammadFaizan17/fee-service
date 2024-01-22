package com.rak.fee.mapper;

import com.rak.fee.dto.FeeDTO;
import com.rak.fee.entity.Fee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface FeeMapper {

    FeeMapper INSTANCE = Mappers.getMapper(FeeMapper.class);

    @Mapping(target = "id", ignore = true)
    Fee toEntity(FeeDTO feeDTO);

    FeeDTO toDTO(Fee fee);
}

