package com.financial.business.entity.conveter;

import com.financial.business.entity.BudgetTracking;
import com.financial.business.entity.dto.BudgetTrackingDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 预算跟踪实体转换器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface BudgetTrackingStructMapper {

    // 转化成dto
    BudgetTrackingDTO toDto(BudgetTracking entity);

    // 转化成entity
    BudgetTracking toEntity(BudgetTrackingDTO dto);

    //转化成dto列表
    List<BudgetTrackingDTO> toDtoList(List<BudgetTracking> entities);

    //转化成entity列表
    List<BudgetTracking> toEntityList(List<BudgetTrackingDTO> dtos);
}