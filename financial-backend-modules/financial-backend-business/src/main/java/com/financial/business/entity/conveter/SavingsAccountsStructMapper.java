package com.financial.business.entity.conveter;

import com.financial.business.entity.SavingsAccounts;
import com.financial.business.entity.dto.SavingsAccountsDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 储蓄卡账户结构映射
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface SavingsAccountsStructMapper {
    // 转化成dto
    SavingsAccountsDTO toDto(SavingsAccounts entity);

    // 转化成entity
    SavingsAccounts toEntity(SavingsAccountsDTO dto);

    // 转化成dto集合
    List<SavingsAccountsDTO> toDtoList(List<SavingsAccounts> entities);

    // 转化成entity集合
    List<SavingsAccounts> toEntityList(List<SavingsAccountsDTO> dtos);
}