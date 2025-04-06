DROP TABLE IF EXISTS income_expense_records;
-- 收支记录表
CREATE TABLE income_expense_records (
                                        id BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
                                        user_id BIGINT NOT NULL COMMENT '用户ID',
                                        type_code BIGINT NOT NULL COMMENT '类型编码',
                                        amount DECIMAL(12, 2) NOT NULL COMMENT '金额',
                                        category_code BIGINT NOT NULL COMMENT '分类',
                                        date DATE NOT NULL COMMENT '日期',
                                        description VARCHAR(255) DEFAULT '' COMMENT '描述',
                                        deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
                                        create_by varchar(64) default '' comment '创建者',
                                        create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                        update_by varchar(64) default '' comment '更新者',
                                        update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                        remark varchar(500) default null comment '备注',
                                        PRIMARY KEY (id),
                                        FOREIGN KEY (type_code) REFERENCES sys_dict_data(dict_code),
                                        FOREIGN KEY (category_code) REFERENCES sys_dict_data(dict_code),
                                        FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '收支记录表';
DROP TABLE IF EXISTS profit_records;
DROP TABLE IF EXISTS holding_details;
-- 持仓明细表
CREATE TABLE holding_details (
                                 id BIGINT NOT NULL AUTO_INCREMENT COMMENT '持仓明细ID',
                                 user_id BIGINT NOT NULL COMMENT '用户ID',
                                 code VARCHAR(50) NOT NULL COMMENT '持仓明细代码',
                                 name VARCHAR(100) NOT NULL COMMENT '名称',
                                 type_code BIGINT NOT NULL COMMENT '类型编码',
                                 quantity DECIMAL(20, 6) NOT NULL COMMENT '持仓金额',
                                 cost DECIMAL(20, 2) NOT NULL COMMENT '成本',
                                 market_value DECIMAL(20, 2) NOT NULL COMMENT '市值',
                                 profit_loss DECIMAL(20, 2) NOT NULL COMMENT '盈亏',
                                 return_rate DECIMAL(10, 4) NOT NULL COMMENT '收益率',
                                 start_date DATE NOT NULL COMMENT '持有起始日期',
                                 end_date DATE COMMENT '持有结束日期',
                                 create_by varchar(64) default '' comment '创建者',
                                 create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                 update_by varchar(64) default '' comment '更新者',
                                 update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                 remark varchar(500) default null comment '备注',
                                 PRIMARY KEY (id),
                                 UNIQUE (user_id, code),
                                 FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                                 FOREIGN KEY (type_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '持仓明细表';
DROP TABLE IF EXISTS transaction_records;
-- 交易记录表
CREATE TABLE transaction_records (
                                     id BIGINT NOT NULL AUTO_INCREMENT COMMENT '交易ID',
                                     user_id BIGINT NOT NULL COMMENT '用户ID',
                                     asset_code VARCHAR(50) NOT NULL COMMENT '资产代码',
                                     type_code BIGINT NOT NULL COMMENT '交易类型编码',
                                     shares DECIMAL(20, 6) NOT NULL COMMENT '交易份额',
                                     price DECIMAL(20, 2) NOT NULL COMMENT '交易价格',
                                     fee DECIMAL(20, 2) DEFAULT 0 COMMENT '交易费用',
                                     transaction_time DATETIME NOT NULL COMMENT '交易时间',
                                     create_by varchar(64) default '' comment '创建者',
                                     create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                     update_by varchar(64) default '' comment '更新者',
                                     update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                     remark varchar(500) default null comment '备注',
                                     PRIMARY KEY (id),
                                     INDEX idx_user_asset (user_id, asset_code),
                                     INDEX idx_transaction_time (transaction_time),
                                     FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                                     FOREIGN KEY (type_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '交易记录表';
DROP TABLE IF EXISTS profit_records;
-- 收益记录表
CREATE TABLE profit_records (
                                id BIGINT NOT NULL AUTO_INCREMENT COMMENT '收益ID',
                                user_id BIGINT NOT NULL COMMENT '用户ID',
                                holding_detail_id BIGINT NOT NULL COMMENT '持仓明细ID',
                                type_code BIGINT NOT NULL COMMENT '收益类型编码',
                                profit_amount DECIMAL(20, 2) NOT NULL COMMENT '收益金额',
                                profit_time DATETIME NOT NULL COMMENT '收益时间',
                                create_by varchar(64) default '' comment '创建者',
                                create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                update_by varchar(64) default '' comment '更新者',
                                update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                remark varchar(500) default null comment '备注',
                                PRIMARY KEY (id),
                                FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                                FOREIGN KEY (holding_detail_id) REFERENCES holding_details(id) ON DELETE CASCADE,
                                FOREIGN KEY (type_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '收益记录表';
DROP TABLE IF EXISTS credit_card_accounts;
-- 信用卡账户表
CREATE TABLE credit_card_accounts (
                                      id BIGINT NOT NULL AUTO_INCREMENT COMMENT '信用卡ID',
                                      user_id BIGINT NOT NULL COMMENT '用户ID',
                                      issuing_bank VARCHAR(100) NOT NULL COMMENT '发卡行',
                                      type_code BIGINT NOT NULL COMMENT '卡片类型编码',
                                      card_number VARCHAR(20) NOT NULL COMMENT '卡号',
                                      credit_limit DECIMAL(20, 2) NOT NULL COMMENT '信用额度',
                                      used_credit DECIMAL(20, 2) NOT NULL COMMENT '已用额度',
                                      available_credit DECIMAL(20, 2) GENERATED ALWAYS AS (credit_limit - used_credit) COMMENT '可用额度',
                                      billing_day TINYINT NOT NULL COMMENT '账单日',
                                      payment_due_day TINYINT NOT NULL COMMENT '还款日',
                                      current_bill DECIMAL(20, 2) NOT NULL COMMENT '当期账单',
                                      status_code BIGINT NOT NULL COMMENT '账户状态代码',
                                      create_by varchar(64) default '' comment '创建者',
                                      create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                      update_by varchar(64) default '' comment '更新者',
                                      update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                      remark varchar(500) default null comment '备注',
                                      PRIMARY KEY (id),
                                      INDEX idx_user_id (user_id),
                                      UNIQUE (card_number),
                                      FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                                      FOREIGN KEY (type_code) REFERENCES sys_dict_data(dict_code),
                                      FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '信用卡账户表';
DROP TABLE IF EXISTS savings_accounts;
-- 储蓄卡账户表
CREATE TABLE savings_accounts (
                                  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '账户ID',
                                  user_id BIGINT NOT NULL COMMENT '用户ID',
                                  bank_name VARCHAR(100) NOT NULL COMMENT '开户行',
                                  type_code BIGINT NOT NULL COMMENT '账户类型编码',
                                  account_number VARCHAR(20) NOT NULL COMMENT '账号',
                                  balance DECIMAL(20, 2) NOT NULL COMMENT '余额',
                                  interest_rate DECIMAL(5, 4) DEFAULT 0 COMMENT '利率',
                                  maturity_date DATE COMMENT '到期日',
                                  status_code BIGINT NOT NULL COMMENT '账户状态代码',
                                  last_transaction_time DATETIME COMMENT '最后交易时间',
                                  create_by varchar(64) default '' comment '创建者',
                                  create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                  update_by varchar(64) default '' comment '更新者',
                                  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                  remark varchar(500) default null comment '备注',
                                  PRIMARY KEY (id),
                                  UNIQUE (account_number),
                                  FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                                  FOREIGN KEY (type_code) REFERENCES sys_dict_data(dict_code),
                                  FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '储蓄卡账户表';
DROP TABLE IF EXISTS e_wallet_accounts;
-- 电子钱包账户表（不确定要不要）
CREATE TABLE e_wallet_accounts (
                                   id BIGINT NOT NULL AUTO_INCREMENT COMMENT '钱包ID',
                                   user_id BIGINT NOT NULL COMMENT '用户ID',
                                   wallet_name VARCHAR(100) NOT NULL COMMENT '钱包名称',
                                   balance DECIMAL(20, 2) NOT NULL COMMENT '余额',
                                   status_code BIGINT NOT NULL COMMENT '账户状态代码',
                                   last_transaction_time DATETIME COMMENT '最后交易时间',
                                   create_by varchar(64) default '' comment '创建者',
                                   create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                   update_by varchar(64) default '' comment '更新者',
                                   update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                   remark varchar(500) default null comment '备注',
                                   PRIMARY KEY (id),
                                   FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                                   FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '电子钱包账户表';
DROP TABLE IF EXISTS loan_records;
-- 借贷记录表
CREATE TABLE loan_records (
                              id BIGINT NOT NULL AUTO_INCREMENT COMMENT '借贷记录ID',
                              user_id BIGINT NOT NULL COMMENT '用户ID',
                              loan_name VARCHAR(100) NOT NULL COMMENT '借贷名称',
                              loan_type_code BIGINT NOT NULL COMMENT '借贷类型编码',
                              principal DECIMAL(20, 2) NOT NULL COMMENT '本金',
                              annual_interest_rate DECIMAL(5, 4) NOT NULL COMMENT '年利率',
                              loan_term INT NOT NULL COMMENT '借款期限（单位：月）',
                              total_interest DECIMAL(20, 2) NOT NULL COMMENT '总利息',
                              status_code BIGINT NOT NULL COMMENT '状态代码',
                              create_by varchar(64) default '' comment '创建者',
                              create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                              update_by varchar(64) default '' comment '更新者',
                              update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                              remark varchar(500) default null comment '备注',
                              PRIMARY KEY (id),
                              FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
                              FOREIGN KEY (loan_type_code) REFERENCES sys_dict_data(dict_code),
                              FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '借贷记录表';
DROP TABLE IF EXISTS repayment_plans;
-- 还款计划表
CREATE TABLE repayment_plans (
                                 id BIGINT NOT NULL AUTO_INCREMENT COMMENT '还款计划ID',
                                 loan_record_id BIGINT NOT NULL COMMENT '借贷记录ID',
                                 repayment_date DATE NOT NULL COMMENT '还款日期',
                                 repayment_amount DECIMAL(20, 2) NOT NULL COMMENT '还款金额',
                                 repayment_progress DECIMAL(5, 2) NOT NULL DEFAULT 0 COMMENT '还款进度（百分比）',
                                 status_code BIGINT NOT NULL COMMENT '状态代码',
                                 create_by varchar(64) default '' comment '创建者',
                                 create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                 update_by varchar(64) default '' comment '更新者',
                                 update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                 remark varchar(500) default null comment '备注',
                                 PRIMARY KEY (id),
                                 FOREIGN KEY (loan_record_id) REFERENCES loan_records(id),
                                 FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '还款计划表';
DROP TABLE IF EXISTS budget_management;
-- 预算管理表
CREATE TABLE budget_management (
                                   id BIGINT NOT NULL AUTO_INCREMENT COMMENT '预算ID',
                                   name VARCHAR(100) NOT NULL COMMENT '预算名称',
                                   type_code BIGINT NOT NULL COMMENT '预算类型编码',
                                   budget_amount DECIMAL(20, 2) NOT NULL COMMENT '预算金额',
                                   budget_period VARCHAR(50) NOT NULL COMMENT '预算周期',
                                   warning_threshold DECIMAL(5, 2) NOT NULL COMMENT '预警阈值（百分比）',
                                   rollover TINYINT NOT NULL COMMENT '是否结转',
                                   status_code BIGINT NOT NULL COMMENT '状态代码',
                                   create_by varchar(64) default '' comment '创建者',
                                   create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                   update_by varchar(64) default '' comment '更新者',
                                   update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                   remark varchar(500) default null comment '备注',
                                   PRIMARY KEY (id),
                                   FOREIGN KEY (type_code) REFERENCES sys_dict_data(dict_code),
                                   FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '预算管理表';
DROP TABLE IF EXISTS budget_tracking;
-- 预算跟踪表
CREATE TABLE budget_tracking (
                                 id BIGINT NOT NULL AUTO_INCREMENT COMMENT '预算跟踪ID',
                                 name VARCHAR(100) NOT NULL COMMENT '预算名称',
                                 budget_type_code BIGINT NOT NULL COMMENT '预算类型编码',
                                 budget_amount DECIMAL(20, 2) NOT NULL COMMENT '预算金额',
                                 used_amount DECIMAL(20, 2) NOT NULL DEFAULT 0 COMMENT '已使用金额',
                                 remaining_amount DECIMAL(20, 2) NOT NULL COMMENT '剩余金额',
                                 usage_rate DECIMAL(5, 2) NOT NULL DEFAULT 0 COMMENT '使用率（百分比）',
                                 status_code BIGINT NOT NULL COMMENT '状态代码',
                                 period_code BIGINT NOT NULL COMMENT '周期代码',
                                 create_by varchar(64) default '' comment '创建者',
                                 create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                 update_by varchar(64) default '' comment '更新者',
                                 update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                 remark varchar(500) default null comment '备注',
                                 PRIMARY KEY (id),
                                 FOREIGN KEY (budget_type_code) REFERENCES sys_dict_data(dict_code),
                                 FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code),
                                 FOREIGN KEY (period_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '预算跟踪表';
DROP TABLE IF EXISTS goal_planning;
-- 目标计划表
CREATE TABLE goal_planning (
                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '目标ID',
                               name VARCHAR(100) NOT NULL COMMENT '目标名称',
                               goal_type_code BIGINT NOT NULL COMMENT '目标类型编码',
                               target_amount DECIMAL(20, 2) NOT NULL COMMENT '目标金额',
                               current_amount DECIMAL(20, 2) NOT NULL DEFAULT 0 COMMENT '当前金额',
                               start_date DATE NOT NULL COMMENT '开始日期',
                               end_date DATE NOT NULL COMMENT '结束日期',
                               status_code BIGINT NOT NULL COMMENT '状态代码',
                               priority_code BIGINT NOT NULL COMMENT '优先级代码',
                               description TEXT COMMENT '描述',
                               create_by varchar(64) default '' comment '创建者',
                               create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                               update_by varchar(64) default '' comment '更新者',
                               update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                               remark varchar(500) default null comment '备注',
                               PRIMARY KEY (id),
                               FOREIGN KEY (goal_type_code) REFERENCES sys_dict_data(dict_code),
                               FOREIGN KEY (status_code) REFERENCES sys_dict_data(dict_code),
                               FOREIGN KEY (priority_code) REFERENCES sys_dict_data(dict_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '目标设定表';