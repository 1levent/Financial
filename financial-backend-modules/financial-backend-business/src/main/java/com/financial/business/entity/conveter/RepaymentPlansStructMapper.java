package com.financial.business.entity.conveter;

import com.financial.business.entity.RepaymentPlans;
import com.financial.business.entity.dto.RepaymentPlansDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 还款计划属性转换器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface RepaymentPlansStructMapper {

    // 转化成dto
    RepaymentPlansDTO toDto(RepaymentPlans entity);

    // 转化成entity
    RepaymentPlans toEntity(RepaymentPlansDTO dto);

    // 集合转化
    List<RepaymentPlansDTO> toDtoList(List<RepaymentPlans> entities);

    List<RepaymentPlans> toEntityList(List<RepaymentPlansDTO> dtos);
}