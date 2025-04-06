package com.financial.business.entity.conveter;

import com.financial.business.entity.CreditCardAccounts;
import com.financial.business.entity.dto.CreditCardAccountsDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 信用卡属性转换器
 * @author xinyi
 * @date 2025/3/29
 */

@Mapper(componentModel = "spring")
public interface CreditCardAccountsStructMapper {

    // 转化成dto
    CreditCardAccountsDTO toDto(CreditCardAccounts entity);

    // 转化成entity
    CreditCardAccounts toEntity(CreditCardAccountsDTO dto);

    // 转化成dto列表
    List<CreditCardAccountsDTO> toDtoList(List<CreditCardAccounts> entities);

    //
    // 转化成entity列表
    List<CreditCardAccounts> toEntityList(List<CreditCardAccountsDTO> dtos);
}