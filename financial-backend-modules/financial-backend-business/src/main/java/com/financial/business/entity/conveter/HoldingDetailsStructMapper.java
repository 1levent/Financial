package com.financial.business.entity.conveter;

import com.financial.business.entity.HoldingDetails;
import com.financial.business.entity.dto.HoldingDetailsDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 持仓明细属性转化器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface HoldingDetailsStructMapper {

    // 转化成dto
    HoldingDetailsDTO toDto(HoldingDetails entity);

    // 转化成entity
    HoldingDetails toEntity(HoldingDetailsDTO dto);

    // 转化成dto集合
    List<HoldingDetailsDTO> toDtoList(List<HoldingDetails> entities);

    // 转化成entity集合
    List<HoldingDetails> toEntityList(List<HoldingDetailsDTO> dtos);
}