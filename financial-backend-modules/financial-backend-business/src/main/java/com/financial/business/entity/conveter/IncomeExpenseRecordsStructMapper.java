package com.financial.business.entity.conveter;

import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.dto.IncomeExpenseRecordsDTO;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 收支记录属性转化器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface IncomeExpenseRecordsStructMapper {

    // 转化成dto
    IncomeExpenseRecordsDTO toDto(IncomeExpenseRecords entity);

    // 转化成entity
    IncomeExpenseRecords toEntity(IncomeExpenseRecordsDTO dto);

    // 转化成dto集合
    List<IncomeExpenseRecordsDTO> toDtoList(List<IncomeExpenseRecords> entities);

    // 转化成entity集合
    List<IncomeExpenseRecords> toEntityList(List<IncomeExpenseRecordsDTO> dtos);
}