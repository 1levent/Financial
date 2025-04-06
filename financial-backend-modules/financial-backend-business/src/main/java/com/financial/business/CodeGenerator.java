package com.financial.business;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.financial.business.entity.BudgetManagement;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器
 * @author xinyi
 */
public class CodeGenerator {
  public static void main(String[] args) {

    List<String> sysTables = Arrays.asList("sys_config", "sys_dict_data", "sys_dict_type","sys_logininfor","sys_menu","sys_notice");
    List<String> businessTables = Arrays.asList(
        "budget_management",
        "budget_tracking",
        "credit_card_accounts",
        "e_wallet_accounts",
        "goal_planning",
        "holding_details",
        "income_expense_records",
        "loan_records",
        "profit_records",
        "repayment_plans",
        "savings_accounts",
        "transaction_records");

    FastAutoGenerator.create("jdbc:mysql://localhost:3307/financial-backend?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8", "root", "wydxb030513")
        .globalConfig(builder -> {
          builder.author("xinyi") // 设置作者
              .enableSwagger() // 开启 swagger
              .outputDir("financial-backend-modules\\financial-backend-business\\src\\main\\java"); // 指定输出目录
        })
        .dataSourceConfig(builder ->
            builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
              int typeCode = metaInfo.getJdbcType().TYPE_CODE;
              if (typeCode == Types.SMALLINT) {
                // 自定义类型转换
                return DbColumnType.INTEGER;
              }
              return typeRegistry.getColumnType(metaInfo);
            })
        )
        .packageConfig(builder ->
            builder.parent("com.financial") // 设置父包名
                .moduleName("business") // 设置父包模块名
                .pathInfo(Collections.singletonMap(OutputFile.xml, "financial-backend-modules\\financial-backend-business\\src\\main\\resources\\mapper\\business")) // 设置mapperXml生成路径
        )
        .strategyConfig(builder ->
            builder.addInclude(businessTables) // 设置需要生成的表名
                .addTablePrefix("t_", "c_") // 设置过滤表前缀
        )
        .execute();
  }
}
