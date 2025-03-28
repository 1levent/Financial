package com.financial.common.datasource.config;

/**
 * 数据源上下文
 *
 * @author xinyi
 */
public class DataSourceContextHolder {
    //使用继承的线程上下文，支持异步时选择传递
    private static final ThreadLocal<String> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    private DataSourceContextHolder(){

    }
    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}