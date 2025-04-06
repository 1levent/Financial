package com.financial.common.security.utils;

import com.financial.system.api.RemoteDictService;
import java.util.Collection;
import java.util.List;
import com.alibaba.fastjson2.JSONArray;
import com.financial.common.core.constant.CacheConstants;
import com.financial.common.core.utils.SpringUtils;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.redis.service.RedisService;
import com.financial.system.api.domain.SysDictData;

/**
 * 字典工具类
 * 
 * @author xinyi
 */
public class DictUtils {
    /**
     * 设置字典缓存
     * 
     * @param key 参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas) {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     * 
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key) {
        JSONArray arrayCache = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(arrayCache)) {
            return arrayCache.toList(SysDictData.class);
        }
        return null;
    }

    /**
     * 删除指定字典缓存
     * 
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        SpringUtils.getBean(RedisService.class).deleteObject(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(CacheConstants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return CacheConstants.SYS_DICT_KEY + configKey;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictValue(String dictType, String dictValue) {
        //先在redis获取获取不到再调用远程服务
        List<SysDictData> dictDatas = getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            for (SysDictData dict : dictDatas) {
                if (dictValue.equals(dict.getDictValue())) {
                    return dict.getDictLabel();
                }
            }
        }
        //调用远程服务
        List<SysDictData> sysDictDataList = SpringUtils.getBean(RemoteDictService.class).dictType(dictType).getData();
        if (StringUtils.isNotEmpty(sysDictDataList)) {
            for (SysDictData dict : sysDictDataList) {
                if (dictValue.equals(dict.getDictValue())) {
                    //设置到redis中
                    setDictCache(dictType, sysDictDataList);
                    System.out.println("调用远程服务获取的字典："+dict.getDictLabel());
                    return dict.getDictLabel();
                }
            }
        }
        return null;
    }
}
