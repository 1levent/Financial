package com.financial.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.financial.system.api.domain.SysDictData;
import com.financial.system.api.domain.SysDictType;

/**
 * 字典 业务层
 * 
 * @author xinyi
 */
public interface ISysDictTypeService extends IService<SysDictType> {


    /**
     * 加载字典缓存数据
     */
    public void loadingDictCache();

    /**
     * 清空字典缓存数据
     */
    public void clearDictCache();

    /**
     * 重置字典缓存数据
     */
    public void resetDictCache();


    /**
     * 校验字典类型称是否唯一
     * 
     * @param dictType 字典类型
     * @return 结果
     */
    public boolean checkDictTypeUnique(SysDictType dictType);

    List<SysDictData> selectDictDataByType(String dictType);
}
