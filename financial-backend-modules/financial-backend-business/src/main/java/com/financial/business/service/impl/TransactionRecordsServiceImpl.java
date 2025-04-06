package com.financial.business.service.impl;

import com.financial.business.entity.TransactionRecords;
import com.financial.business.mapper.TransactionRecordsMapper;
import com.financial.business.service.ITransactionRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易记录表 服务实现类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@Service
public class TransactionRecordsServiceImpl extends ServiceImpl<TransactionRecordsMapper, TransactionRecords> implements ITransactionRecordsService {

}
