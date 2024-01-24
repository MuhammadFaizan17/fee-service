package com.rak.fee.mapper;

import com.rak.fee.dto.FeeDTO;
import com.rak.fee.entity.Fee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FeeMapper {


    @Mapping(target = "id", ignore = true)
    Fee toEntity(FeeDTO feeDTO);

    FeeDTO toDTO(Fee fee);
}

