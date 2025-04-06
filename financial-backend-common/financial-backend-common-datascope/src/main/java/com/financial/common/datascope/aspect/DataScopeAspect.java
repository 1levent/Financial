package com.financial.common.datascope.aspect;

import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import com.financial.common.core.constant.UserConstants;
import com.financial.common.core.context.SecurityContextHolder;
import com.financial.common.core.text.Convert;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.core.web.domain.BaseEntity;
import com.financial.common.datascope.annotation.DataScope;
import com.financial.common.security.utils.SecurityUtils;
import com.financial.system.api.domain.SysRole;
import com.financial.system.api.domain.SysUser;
import com.financial.system.api.model.LoginUser;

/**
 * 数据过滤处理
 * todo 数据过滤处理要重新设计，因为去除了部门因素
 * @author xinyi
 */
@Aspect
@Component
public class DataScopeAspect {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable
    {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser))
        {
            SysUser currentUser = loginUser.getSysUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin())
            {
                String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), SecurityContextHolder.getPermission());
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.userAlias(), permission);
            }
        }
    }

    /**
     * 数据范围过滤
     * 
     * @param joinPoint 切点
     * @param user 用户
     * @param userAlias 用户别名
     * @param permission 权限字符
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String userAlias, String permission) {
        StringBuilder sqlString = new StringBuilder();
        List<String> conditions = new ArrayList<String>();
        List<String> scopeCustomIds = new ArrayList<String>();
        user.getRoles().forEach(role -> {
            if (DATA_SCOPE_CUSTOM.equals(role.getDataScope()) && StringUtils.equals(role.getStatus(), UserConstants.ROLE_NORMAL) && StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission)))
            {
                scopeCustomIds.add(Convert.toStr(role.getRoleId()));
            }
        });

        for (SysRole role : user.getRoles())
        {
            String dataScope = role.getDataScope();
            if (conditions.contains(dataScope) || StringUtils.equals(role.getStatus(), UserConstants.ROLE_DISABLE))
            {
                continue;
            }
            if (!StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission)))
            {
                continue;
            }
            if (DATA_SCOPE_ALL.equals(dataScope))
            {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            }
            else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias))
                {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                }
            }
            conditions.add(dataScope);
        }
        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
            {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint)
    {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
        {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
