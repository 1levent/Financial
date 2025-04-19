package com.financial.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.security.utils.DictUtils;
import jakarta.annotation.Resource;

import java.util.List;
import org.springframework.stereotype.Service;
import com.financial.system.api.domain.SysDictData;
import com.financial.system.mapper.SysDictDataMapper;
import com.financial.system.service.ISysDictDataService;

/**
 * 字典 业务层处理
 * 
 * @author xinyi
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {
    @Resource
    private SysDictDataMapper dictDataMapper;

    /**
     * 保存字典数据
     * @param dict
     * @return
     */
    @Override
    public boolean addDict(SysDictData dict) {
        //首先我要先查询该条记录是否已经存在表中，若是存在，则不进行保存
        //认定为同一条记录的依据是dictType和dictLabel以及dictValue相同
        QueryWrapper<SysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type",dict.getDictType());
        queryWrapper.eq("dict_label",dict.getDictLabel());
        queryWrapper.eq("dict_value",dict.getDictValue());
        if(dictDataMapper.selectOne(queryWrapper)!=null){
            System.out.println("该条记录已经存在表中，请勿重复添加");
            return false;
        }
        //新增字典之后，要把它存放到缓存中
        if(dictDataMapper.insert(dict)>0){
            //先获取字典缓存
            List<SysDictData> dictCache = DictUtils.getDictCache(dict.getDictType());
            if (dictCache != null) {
                dictCache.add(dict);
            }
            //更新缓存
            DictUtils.setDictCache(dict.getDictType(),dictCache);
            return true;
        }
        return false;
    }
}
