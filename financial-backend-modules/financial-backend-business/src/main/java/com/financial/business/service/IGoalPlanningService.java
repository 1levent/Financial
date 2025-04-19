package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.GoalPlanning;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.GoalPlanningDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 目标设定表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface IGoalPlanningService extends IService<GoalPlanning> {
  public IPage<GoalPlanningDTO> list(GoalPlanningDTO dto, Page<GoalPlanning> page);
  public void export(GoalPlanningDTO dto, HttpServletResponse response);
}
