package com.financial.business.entity.conveter;

import com.financial.business.entity.EWalletAccounts;
import com.financial.business.entity.dto.EWalletAccountsDTO;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * 电子钱包账户属性转换器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface EWalletAccountsStructMapper {

    // 转化成dto
    EWalletAccountsDTO toDto(EWalletAccounts entity);

    // 转化成entity
    EWalletAccounts toEntity(EWalletAccountsDTO dto);

    // 批量转化成dto
    List<EWalletAccountsDTO> toDtoList(List<EWalletAccounts> entitesList);

    // 批量转化成entity
    List<EWalletAccounts> toEntityList(List<EWalletAccountsDTO> dtosList);
}