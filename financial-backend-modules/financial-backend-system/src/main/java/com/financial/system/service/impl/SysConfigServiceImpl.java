package com.financial.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import com.financial.common.core.constant.CacheConstants;
import com.financial.common.core.constant.UserConstants;
import com.financial.common.core.text.Convert;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.redis.service.RedisService;
import com.financial.system.domain.SysConfig;
import com.financial.system.mapper.SysConfigMapper;
import com.financial.system.service.ISysConfigService;

/**
 * 参数配置 服务层实现
 * 
 * @author xinyi
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
    @Resource
    private SysConfigMapper configMapper;

    @Resource
    private RedisService redisService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisService.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
            .eq(SysConfig::getConfigKey, configKey));
        if (StringUtils.isNotNull(retConfig)) {
            redisService.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }



    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = configMapper.selectList(new LambdaQueryWrapper<SysConfig>());
        for (SysConfig config : configsList) {
            redisService.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        Collection<String> keys = redisService.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisService.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        Long count = configMapper.selectCount(new LambdaQueryWrapper<SysConfig>()
            .eq(SysConfig::getConfigKey, config.getConfigKey())
            .ne(config.getConfigId() != null, SysConfig::getConfigId, config.getConfigId()));
        if (count!=0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
