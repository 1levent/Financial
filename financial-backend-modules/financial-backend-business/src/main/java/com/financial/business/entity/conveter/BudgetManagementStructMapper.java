package com.financial.business.entity.conveter;

import com.financial.business.entity.BudgetManagement;
import com.financial.business.entity.dto.BudgetManagementDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * 属性映射器
 * @author xinyi
 * @date 2025/3/29
 */
@Mapper(componentModel = "spring")
public interface BudgetManagementStructMapper {

  //转化成dto
  BudgetManagementDTO toDto(BudgetManagement entity);

  //转化成entity
  BudgetManagement toEntity(BudgetManagementDTO dto);

  List<BudgetManagementDTO> toDtoList(List<BudgetManagement> entities);

  List<BudgetManagement> toEntityList(List<BudgetManagementDTO> dtos);

}
