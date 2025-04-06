package com.financial.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.security.utils.DictUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.financial.common.core.constant.UserConstants;
import com.financial.common.core.utils.StringUtils;
import com.financial.system.api.domain.SysDictData;
import com.financial.system.api.domain.SysDictType;
import com.financial.system.mapper.SysDictDataMapper;
import com.financial.system.mapper.SysDictTypeMapper;
import com.financial.system.service.ISysDictDataService;
import com.financial.system.service.ISysDictTypeService;

/**
 * 字典 业务层处理
 * 
 * @author xinyi
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Resource
    private ISysDictDataService dictDataService;

    @Resource
    private SysDictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init()
    {
        loadingDictCache();
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        QueryWrapper<SysDictData> query = new QueryWrapper<>();
        query.eq("status", "0");
        Map<String, List<SysDictData>> dictDataMap  = dictDataService.list(query).stream()
            .collect(Collectors.groupingBy(SysDictData::getDictType));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType dict) {
        QueryWrapper<SysDictType> query = new QueryWrapper<>();
        query.eq("dict_type", dict.getDictType());
        query.ne("dict_id", dict.getDictId());
        List<SysDictType> list = this.list(query);
        if (StringUtils.isNotEmpty(list)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据字典类型查询字典数据
     * @param dictType 字典类型
     * @return 结果
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        //先去缓存中查询
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        QueryWrapper<SysDictData> query = new QueryWrapper<>();
        query.eq("dict_type", dictType);
        dictDatas = dictDataMapper.selectList(query);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return new ArrayList<SysDictData>();
    }
}
