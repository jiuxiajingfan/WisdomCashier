```sql
create database wisdomcashier;   
```

### 表设计
```sql
create table t_user(
                       id bigint primary key  comment 'id主键',
                       user_name varchar(40) comment '用户登录账户',
                       user_pwd varchar(32) comment '用户登录密码md5',
                       user_nickname varchar(20) comment '用户名',
                       email varchar(40) comment '邮箱',
                       gmt_create varchar(30),
                       gmt_update varchar(30),
                       status integer comment '账户状态 0正常 1封禁 2注销',
                       phone varchar(11) comment '11位手机号'
)

create table t_shop(
                       id bigint primary key comment 'id主键',
                       shop_name varchar(20) comment '店铺名',
                       gmt_create varchar(30),
                       gmt_update varchar(30),
                       tip text comment '介绍',
                       status integer comment '店铺状态 0正常 1封禁 2注销'
)

create table t_role(
                       id bigint auto_increment primary key comment 'id主键',
                       user_id bigint,
                       shop_id bigint,
                       role int comment '角色 0为普通用户 1为收银员 2为店主 3为超级管理员'
)

create table t_permission(
                             id int primary key auto_increment ,
                             role int,
                             permission varchar(20)
)
```