package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.Account;
import com.financial.business.entity.conveter.AccountStructMapper;
import com.financial.business.entity.dto.AccountDTO;
import com.financial.business.entity.dto.statistic.AccountPieDTO;
import com.financial.business.service.IAccountService;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 账户表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/business/account")
@Tag(name = "账户表")
public class AccountController extends BaseController {

    @Resource
    private IAccountService accountService;

    @Resource
    private AccountStructMapper accountStructMapper;

    /**
     * 获取账户列表
     */
    @Operation(summary = "获取账户列表")
    @PostMapping("/list")
    public PageResponse<AccountDTO> list(
        @RequestBody AccountDTO accountDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<Account> page = new Page<>(pageNum, pageSize);
        accountDTO.setUserId(SecurityUtils.getUserId());
        IPage<AccountDTO> result = accountService.list(accountDTO, page);
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

    /**
     * 根据账户编号获取详细信息
     */
    @Operation(summary = "根据账户编号获取详细信息")
    @GetMapping(value = "/{id}")
    public R<AccountDTO> getInfo(@PathVariable Long id) {
        AccountDTO accountDTO = accountStructMapper.toDto(accountService.getById(id));
        return R.ok(accountDTO);
    }

    /**
     * 新增账户
     */
    @Operation(summary = "新增账户")
    @Log(title = "账户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AccountDTO accountDTO) {
        accountDTO.setUserId(SecurityUtils.getUserId());
        //要判断账号是不是已经有了，有了不可以添加
        Account account1 = accountService.getByAccountNo(accountDTO.getAccountNo());
        if (account1 != null) {
            return AjaxResult.error("该账户已经存在");
        }
        Account account = accountStructMapper.toEntity(accountDTO);
        return toAjax(accountService.save(account));
    }

    /**
     * 修改账户
     */
    @Operation(summary = "修改账户")
    @Log(title = "账户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AccountDTO accountDTO) {
        accountDTO.setUserId(SecurityUtils.getUserId());
        //要判断账号是不是已经有了，有了不可以添加
        if (accountService.getByAccountNo(accountDTO.getAccountNo()) != null) {
            return AjaxResult.error("该账户已经存在");
        }
        Account account = accountStructMapper.toEntity(accountDTO);
        return toAjax(accountService.updateById(account));
    }

    /**
     * 删除账户
     */
    @Operation(summary = "删除账户")
    @Log(title = "账户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(accountService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 导出账户列表
     */
    @Operation(summary = "导出账户列表")
    @Log(title = "账户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody AccountDTO accountDTO) throws IOException, UnsupportedEncodingException {
        // 获取数据并导出
        accountDTO.setUserId(SecurityUtils.getUserId());
        accountService.export(accountDTO, ExcelUtils.setFileName(response, "账户数据"));
    }

    @Operation(summary = "获取账户饼图")
    @GetMapping("/getAccountPie")
    public R<List<AccountPieDTO>> getAccountPie() {
        //要去除银行卡类型的数据
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.notIn("account_type", "2");
        queryWrapper.eq("user_id", SecurityUtils.getUserId());
        List<Account> accounts = accountService.list(queryWrapper);
        //聚合数据成饼图
        List<AccountPieDTO> accountPieDTOS = new ArrayList<>();
        for (Account account : accounts) {
            AccountPieDTO accountPieDTO = new AccountPieDTO();
            accountPieDTO.setAccountNo(account.getAccountNo());
            accountPieDTO.setType(account.getAccountType());
            accountPieDTO.setAmount(account.getAmount().doubleValue());
            accountPieDTOS.add(accountPieDTO);
        }
        return R.ok(accountPieDTOS);
    }
}