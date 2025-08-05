package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.LoanRecords;
import com.financial.business.entity.conveter.LoanRecordsStructMapper;
import com.financial.business.entity.dto.LoanRecordsDTO;
import com.financial.business.entity.dto.statistic.LoanStatisticDTO;
import com.financial.business.service.ILoanRecordsService;
import com.financial.common.core.domain.R;
import com.financial.common.core.utils.excel.ExcelUtils;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.PageResponse;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 借贷记录表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/loanRecords")
@Tag(name = "借贷记录表")
public class LoanRecordsController extends BaseController {

    @Resource
    private ILoanRecordsService loanRecordsService;

    @Resource
    private LoanRecordsStructMapper loanRecordsStructMapper;

    /**
     * 获取借贷记录列表
     */
    @Operation(summary = "获取借贷记录列表")
//    @RequiresPermissions("business:loanRecords:list")
    @PostMapping("/list")
    public PageResponse<LoanRecordsDTO> list(
        @RequestBody LoanRecordsDTO loanRecordsDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<LoanRecords> page = new Page<>(pageNum, pageSize);
        loanRecordsDTO.setUserId(SecurityUtils.getUserId());
        IPage<LoanRecordsDTO> result = loanRecordsService.list(loanRecordsDTO, page);
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

    @Operation(summary = "导出借贷记录列表")
//    @RequiresPermissions("business:loanRecords:export")
    @Log(title = "借贷记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LoanRecordsDTO loanRecordsDTO)
        throws IOException, UnsupportedEncodingException {
        loanRecordsDTO.setUserId(SecurityUtils.getUserId());
        loanRecordsService.export(loanRecordsDTO, ExcelUtils.setFileName(response, "借贷记录"));
    }

    /**
     * 根据借贷记录编号获取详细信息
     */
    @Operation(summary = "根据借贷记录编号获取详细信息")
//    @RequiresPermissions("business:loanRecords:query")
    @GetMapping(value = "/{id}")
    public R<LoanRecordsDTO> getInfo(@PathVariable Long id) {
        LoanRecords loanRecords = loanRecordsService.getById(id);
        if (loanRecords == null) {
            return R.fail("该记录不存在");
        }
        LoanRecordsDTO loanRecordsDTO = loanRecordsStructMapper.toDto(loanRecords);
        return R.ok(loanRecordsDTO);
    }

    /**
     * 新增借贷记录
     */
    @Operation(summary = "新增借贷记录")
//    @RequiresPermissions("business:loanRecords:add")
    @Log(title = "借贷记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody LoanRecordsDTO loanRecordsDTO) {
        loanRecordsDTO.setUserId(SecurityUtils.getUserId());
        //根据本金、年利率、借款期限计算总利息，写一个方法来计算
        BigDecimal totalInterest =loanRecordsService.calculateTotalInterest(loanRecordsDTO);
        loanRecordsDTO.setTotalInterest(totalInterest);
        LoanRecords loanRecords = loanRecordsStructMapper.toEntity(loanRecordsDTO);
        return toAjax(loanRecordsService.save(loanRecords));
    }

    /**
     * 修改借贷记录
     */
    @Operation(summary = "修改借贷记录")
//    @RequiresPermissions("business:loanRecords:edit")
    @Log(title = "借贷记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody LoanRecordsDTO loanRecordsDTO) {
        loanRecordsDTO.setUserId(SecurityUtils.getUserId());
        BigDecimal totalInterest =loanRecordsService.calculateTotalInterest(loanRecordsDTO);
        loanRecordsDTO.setTotalInterest(totalInterest);
        LoanRecords loanRecords = loanRecordsStructMapper.toEntity(loanRecordsDTO);
        return toAjax(loanRecordsService.updateById(loanRecords));
    }

    /**
     * 删除借贷记录
     */
    @Operation(summary = "删除借贷记录")
//    @RequiresPermissions("business:loanRecords:remove")
    @Log(title = "借贷记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(loanRecordsService.removeBatchByIds(Arrays.asList(ids)));
    }

    @Operation(summary = "计算借贷记录统计信息")
    @GetMapping("/calculateLoanStatistics")
    public R<LoanStatisticDTO> calculateLoanStatistics() {
        LoanRecords loan = new LoanRecords();
        loan.setUserId(SecurityUtils.getUserId());
        List<LoanRecords> list = loanRecordsService.list(new QueryWrapper<>(loan));
        return R.ok(loanRecordsService.calculateLoanStatistics(list));
    }
}