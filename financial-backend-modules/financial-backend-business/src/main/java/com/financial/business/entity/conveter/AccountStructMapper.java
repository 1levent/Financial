package com.financial.business.entity.conveter;

import com.financial.business.entity.Account;
import com.financial.business.entity.dto.AccountDTO;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * @author xinyi
 */
@Mapper(componentModel = "spring")
public interface AccountStructMapper {
  //转化成dto
  AccountDTO toDto(Account entity);
  //转化成entity
  Account toEntity(AccountDTO dto);

  List<AccountDTO> toDtoList(List<Account> entities);

  List<Account> toEntityList(List<AccountDTO> dtos);

}
