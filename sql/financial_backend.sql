SET NAMES utf8mb4;
DROP TABLE IF EXISTS income_expense_records;
-- 收支记录表
CREATE TABLE income_expense_records (
                                        id BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
                                        user_id BIGINT NOT NULL COMMENT '用户ID',
                                        type_code varchar(100) NOT NULL COMMENT '类型编码',
                                        amount DECIMAL(12, 2) NOT NULL COMMENT '金额',
                                        category_code varchar(100) NOT NULL COMMENT '分类',
                                        date DATE NOT NULL COMMENT '日期',
                                        description VARCHAR(255) DEFAULT '' COMMENT '描述',
                                        deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
                                        create_by varchar(64) default '' comment '创建者',
                                        create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                                        update_by varchar(64) default '' comment '更新者',
                                        update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                                        remark varchar(500) default null comment '备注',
                                        PRIMARY KEY (id),
                                        FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) engine = innodb auto_increment = 1 COMMENT = '收支记录表';
DROP TABLE IF EXISTS holding_details;
-- 持仓明细表
CREATE TABLE holding_details (
                                 id BIGINT NOT NULL AUTO_INCREMENT COMMENT '持仓明细ID',
                                 user_id BIGINT NOT NULL COMMENT '用户ID',
                                 code VARCHAR(50) NOT NULL COMMENT '持仓明细代码',
                                 name VARCHAR(100) NOT NULL COMMENT '名称',
                                 type_code VARCHAR(100) NOT NULL COMMENT '类型编码',
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
                                 FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE = InnoDB auto_increment = 1 COMMENT = '持仓明细表';
DROP TABLE IF EXISTS transaction_records;
-- 交易记录表
CREATE TABLE transaction_records (
                                     id BIGINT NOT NULL AUTO_INCREMENT COMMENT '交易ID',
                                     user_id BIGINT NOT NULL COMMENT '用户ID',
                                     asset_code VARCHAR(50) NOT NULL COMMENT '资产代码',
                                     type_code VARCHAR(100) NOT NULL COMMENT '交易类型编码',
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
                                     FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE = InnoDB auto_increment = 1 COMMENT = '交易记录表';
drop table if exists profit_records;
-- 收益记录表
create table profit_records (
                                id bigint not null auto_increment comment '收益id',
                                user_id bigint not null comment '用户id',
                                holding_id bigint not null comment '持仓明细id',
                                type_code varchar(100) not null comment '收益类型编码',
                                profit_amount decimal(20, 2) not null comment '收益金额',
                                profit_time datetime not null comment '收益时间',
                                create_by varchar(64) default '' comment '创建者',
                                create_time datetime default current_timestamp comment '创建时间',
                                update_by varchar(64) default '' comment '更新者',
                                update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                                remark varchar(500) default null comment '备注',
                                primary key (id),
                                foreign key (user_id) references sys_user(user_id),
                                foreign key (holding_id) references holding_details(id) on delete cascade
) engine = innodb dauto_increment = 1 comment = '收益记录表';
drop table if exists credit_card_accounts;
-- 信用卡账户表
create table credit_card_accounts (
                                      id bigint not null auto_increment comment '信用卡id',
                                      user_id bigint not null comment '用户id',
                                      issuing_bank varchar(100) not null comment '发卡行',
                                      type_code varchar(100) not null comment '卡片类型编码',
                                      card_number varchar(20) not null comment '卡号',
                                      credit_limit decimal(20, 2) not null comment '信用额度',
                                      used_credit decimal(20, 2) not null comment '已用额度',
                                      available_credit decimal(20, 2) generated always as (credit_limit - used_credit) comment '可用额度',
                                      billing_day tinyint not null comment '账单日',
                                      payment_due_day tinyint not null comment '还款日',
                                      current_bill decimal(20, 2) not null comment '当期账单',
                                      status_code varchar(100) not null comment '账户状态代码',
                                      create_by varchar(64) default '' comment '创建者',
                                      create_time datetime default current_timestamp comment '创建时间',
                                      update_by varchar(64) default '' comment '更新者',
                                      update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                                      remark varchar(500) default null comment '备注',
                                      primary key (id),
                                      index idx_user_id (user_id),
                                      unique (card_number),
                                      foreign key (user_id) references sys_user(user_id)
) engine = innodb auto_increment = 1 comment = '信用卡账户表';
drop table if exists savings_accounts;
-- 储蓄卡账户表
create table savings_accounts (
                                  id bigint not null auto_increment comment '账户id',
                                  user_id bigint not null comment '用户id',
                                  bank_name varchar(100) not null comment '开户行',
                                  type_code varchar(100) not null comment '账户类型编码',
                                  account_number varchar(20) not null comment '账号',
                                  balance decimal(20, 2) not null comment '余额',
                                  interest_rate decimal(5, 4) default 0 comment '利率',
                                  maturity_date date comment '到期日',
                                  status_code varchar(100) not null comment '账户状态代码',
                                  last_transaction_time datetime comment '最后交易时间',
                                  create_by varchar(64) default '' comment '创建者',
                                  create_time datetime default current_timestamp comment '创建时间',
                                  update_by varchar(64) default '' comment '更新者',
                                  update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                                  remark varchar(500) default null comment '备注',
                                  primary key (id),
                                  unique (account_number),
                                  foreign key (user_id) references sys_user(user_id)
) engine = innodb auto_increment = 1 comment = '储蓄卡账户表';
drop table if exists e_wallet_accounts;
-- 电子钱包账户表（不确定要不要）
create table e_wallet_accounts (
                                   id bigint not null auto_increment comment '钱包id',
                                   user_id bigint not null comment '用户id',
                                   wallet_name varchar(100) not null comment '钱包名称',
                                   balance decimal(20, 2) not null comment '余额',
                                   status_code varchar(100) not null comment '账户状态代码',
                                   last_transaction_time datetime comment '最后交易时间',
                                   create_by varchar(64) default '' comment '创建者',
                                   create_time datetime default current_timestamp comment '创建时间',
                                   update_by varchar(64) default '' comment '更新者',
                                   update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                                   remark varchar(500) default null comment '备注',
                                   primary key (id),
                                   foreign key (user_id) references sys_user(user_id)
) engine = innodb auto_increment = 1 comment = '电子钱包账户表';
drop table if exists loan_records;
-- 借贷记录表
create table loan_records (
                              id bigint not null auto_increment comment '借贷记录id',
                              user_id bigint not null comment '用户id',
                              loan_name varchar(100) not null comment '借贷名称',
                              loan_type_code varchar(100) not null comment '借贷类型编码',
                              principal decimal(20, 2) not null comment '本金',
                              annual_interest_rate decimal(5, 4) not null comment '年利率',
                              loan_term int not null comment '借款期限（单位：月）',
                              total_interest decimal(20, 2) not null comment '总利息',
                              status_code varchar(100) not null comment '状态代码',
                              create_by varchar(64) default '' comment '创建者',
                              create_time datetime default current_timestamp comment '创建时间',
                              update_by varchar(64) default '' comment '更新者',
                              update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                              remark varchar(500) default null comment '备注',
                              primary key (id),
                              foreign key (user_id) references sys_user(user_id)
) engine = innodb auto_increment = 1 comment = '借贷记录表';
drop table if exists repayment_plans;
-- 还款计划表
create table repayment_plans (
                                 id bigint not null auto_increment comment '还款计划id',
                                 loan_record_id bigint not null comment '借贷记录id',
                                 repayment_date date not null comment '还款日期',
                                 repayment_amount decimal(20, 2) not null comment '还款金额',
                                 repayment_progress decimal(5, 2) not null default 0 comment '还款进度（百分比）',
                                 status_code varchar(100) not null comment '状态代码',
                                 create_by varchar(64) default '' comment '创建者',
                                 create_time datetime default current_timestamp comment '创建时间',
                                 update_by varchar(64) default '' comment '更新者',
                                 update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                                 remark varchar(500) default null comment '备注',
                                 primary key (id),
                                 foreign key (loan_record_id) references loan_records(id)
) engine = innodb auto_increment = 1 comment = '还款计划表';
drop table if exists budget_management;
-- 预算管理表
create table budget_management (
                                   id bigint not null auto_increment comment '预算id',
                                   name varchar(100) not null comment '预算名称',
                                   type_code varchar(100) not null comment '预算类型编码',
                                   budget_amount decimal(20, 2) not null comment '预算金额',
                                   budget_period varchar(50) not null comment '预算周期',
                                   warning_threshold decimal(5, 2) not null comment '预警阈值（百分比）',
                                   rollover tinyint not null comment '是否结转',
                                   status_code varchar(100) not null comment '状态代码',
                                   create_by varchar(64) default '' comment '创建者',
                                   create_time datetime default current_timestamp comment '创建时间',
                                   update_by varchar(64) default '' comment '更新者',
                                   update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                                   remark varchar(500) default null comment '备注',
                                   primary key (id)
) engine = innodb auto_increment = 1 comment = '预算管理表';
drop table if exists budget_tracking;
-- 预算跟踪表
create table budget_tracking (
                                 id bigint not null auto_increment comment '预算跟踪id',
                                 budget_id bigint not null comment '预算id',
                                 used_amount decimal(20, 2) not null default 0 comment '已使用金额',
                                 remaining_amount decimal(20, 2) not null comment '剩余金额',
                                 usage_rate decimal(5, 2) not null default 0 comment '使用率（百分比）',
                                 status_code varchar(100) not null comment '状态代码',
                                 start_time datetime not null comment '开始时间',
                                 end_time datetime not null comment '结束时间',
                                 create_by varchar(64) default '' comment '创建者',
                                 create_time datetime default current_timestamp comment '创建时间',
                                 update_by varchar(64) default '' comment '更新者',
                                 update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                                 remark varchar(500) default null comment '备注',
                                 primary key (id),
                                 foreign key (budget_id) references budget_management(id)
) engine = innodb auto_increment = 1 comment = '预算跟踪表';
drop table if exists goal_planning;
-- 目标计划表
create table goal_planning (
                               id bigint not null auto_increment comment '目标id',
                               name varchar(100) not null comment '目标名称',
                               goal_type_code varchar(100) not null comment '目标类型编码',
                               target_amount decimal(20, 2) not null comment '目标金额',
                               current_amount decimal(20, 2) not null default 0 comment '当前金额',
                               start_date date not null comment '开始日期',
                               end_date date not null comment '结束日期',
                               status_code varchar(100) not null comment '状态代码',
                               priority_code varchar(100) not null comment '优先级代码',
                               description text comment '描述',
                               create_by varchar(64) default '' comment '创建者',
                               create_time datetime default current_timestamp comment '创建时间',
                               update_by varchar(64) default '' comment '更新者',
                               update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
                               remark varchar(500) default null comment '备注',
                               primary key (id)
) engine = innodb auto_increment = 1 comment = '目标设定表';

-- 账户表
CREATE table account(
                        id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
                        account_no VARCHAR(32) NOT NULL COMMENT '账户号',
                        account_type VARCHAR(100) not null comment '账户类型',
                        amount DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '资产',
                        deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
                        create_by varchar(64) default '' comment '创建者',
                        create_time datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间',
                        update_by varchar(64) default '' comment '更新者',
                        update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
                        remark varchar(500) default null comment '备注',
                        PRIMARY KEY (id)
)engine = innodb auto_increment = 1 COMMENT = '账户表';