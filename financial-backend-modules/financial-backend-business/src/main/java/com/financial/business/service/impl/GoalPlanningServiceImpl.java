package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.GoalPlanning;
import com.financial.business.entity.conveter.GoalPlanningStructMapper;
import com.financial.business.entity.dto.GoalPlanningDTO;
import com.financial.business.mapper.GoalPlanningMapper;
import com.financial.business.service.IGoalPlanningService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoalPlanningServiceImpl extends ServiceImpl<GoalPlanningMapper, GoalPlanning> implements IGoalPlanningService {

    @Resource
    private GoalPlanningStructMapper goalPlanningStructMapper;

    @Override
    public IPage<GoalPlanningDTO> list(GoalPlanningDTO dto, Page<GoalPlanning> page) {
        GoalPlanning entity = goalPlanningStructMapper.toEntity(dto);
        QueryWrapper<GoalPlanning> queryWrapper = new QueryWrapper<>(entity);

//        if (dto.getStartDate() != null && dto.getEndDate() != null) {
//            queryWrapper.between("date", dto.getStartDate(), dto.getEndDate());
//        }
        Page<GoalPlanning> result = page(page, queryWrapper);
        return result.convert(goalPlanningStructMapper::toDto);
    }

    @Override
    public void export(GoalPlanningDTO dto, HttpServletResponse response) {
        QueryWrapper<GoalPlanning> queryWrapper = new QueryWrapper<>();
//        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
//            queryWrapper.in("id", dto.getIds());
//        }
        Page<GoalPlanning> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<GoalPlanning> result = page(page, queryWrapper);
        List<GoalPlanningDTO> dtoList = goalPlanningStructMapper.toDtoList(result.getRecords());
        try {
            FastExcel.write(response.getOutputStream(), GoalPlanningDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("目标规划")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}