package com.financial.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.financial.system.domain.SysConfig;

/**
 * 参数配置 服务层
 * 
 * @author xinyi
 */
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);

    void loadingConfigCache();

    void clearConfigCache();

    void resetConfigCache();

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数信息
     * @return 结果
     */
    public boolean checkConfigKeyUnique(SysConfig config);
}
