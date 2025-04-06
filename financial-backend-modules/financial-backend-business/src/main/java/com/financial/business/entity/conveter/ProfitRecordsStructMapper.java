package com.financial.business.entity.conveter;

import com.financial.business.entity.ProfitRecords;
import com.financial.business.entity.dto.ProfitRecordsDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 收益记录属性转换器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface ProfitRecordsStructMapper {

    // 转化成dto
    ProfitRecordsDTO toDto(ProfitRecords entity);

    // 转化成entity
    ProfitRecords toEntity(ProfitRecordsDTO dto);

    // 转化成dto列表
    List<ProfitRecordsDTO> toDtoList(List<ProfitRecords> entities);

    // 转化成entity列表
    List<ProfitRecords> toEntityList(List<ProfitRecordsDTO> dtos);
}