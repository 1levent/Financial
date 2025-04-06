package com.financial.business.service.impl;

import com.financial.business.entity.HoldingDetails;
import com.financial.business.mapper.HoldingDetailsMapper;
import com.financial.business.service.IHoldingDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 持仓明细表 服务实现类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@Service
public class HoldingDetailsServiceImpl extends ServiceImpl<HoldingDetailsMapper, HoldingDetails> implements IHoldingDetailsService {

}
