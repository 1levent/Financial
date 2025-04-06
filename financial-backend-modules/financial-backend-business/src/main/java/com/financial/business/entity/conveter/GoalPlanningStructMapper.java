package com.financial.business.entity.conveter;

import com.financial.business.entity.GoalPlanning;
import com.financial.business.entity.dto.GoalPlanningDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 目标规划实体转换器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface GoalPlanningStructMapper {

    // 转化成dto
    GoalPlanningDTO toDto(GoalPlanning entity);

    // 转化成entity
    GoalPlanning toEntity(GoalPlanningDTO dto);

    // 转化成dto列表
    List<GoalPlanningDTO> toDtoList(List<GoalPlanning> entities);

    // 转化成entity列表
    List<GoalPlanning> toEntityList(List<GoalPlanningDTO> dtos);
}