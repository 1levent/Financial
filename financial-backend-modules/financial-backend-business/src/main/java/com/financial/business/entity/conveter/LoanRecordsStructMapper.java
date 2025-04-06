package com.financial.business.entity.conveter;

import com.financial.business.entity.LoanRecords;
import com.financial.business.entity.dto.LoanRecordsDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 借贷记录属性转化器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface LoanRecordsStructMapper {

    // 转化成dto
    LoanRecordsDTO toDto(LoanRecords entity);

    // 转化成entity
    LoanRecords toEntity(LoanRecordsDTO dto);

    // 转化成dto集合
    List<LoanRecordsDTO> toDtoList(List<LoanRecords> entities);

    // 转化成entity集合
    List<LoanRecords> toEntityList(List<LoanRecordsDTO> dtos);
}