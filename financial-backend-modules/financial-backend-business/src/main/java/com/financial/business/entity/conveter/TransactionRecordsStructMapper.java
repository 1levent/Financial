package com.financial.business.entity.conveter;

import com.financial.business.entity.TransactionRecords;
import com.financial.business.entity.dto.TransactionRecordsDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 交易记录属性转换器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface TransactionRecordsStructMapper {
    // 转化成dto
    TransactionRecordsDTO toDto(TransactionRecords entity);

    // 转化成entity
    TransactionRecords toEntity(TransactionRecordsDTO dto);

    // 批量转化
    List<TransactionRecordsDTO> toDtoList(List<TransactionRecords> entities);

    List<TransactionRecords> toEntityList(List<TransactionRecordsDTO> dtos);
}