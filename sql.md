```sql
create database wisdomcashier;   
```

### 表设计
```sql
-- auto-generated definition
create table t_apply
(
    id         bigint auto_increment
        primary key,
    user_id    bigint   null,
    shop_id    bigint   null,
    status     int      null,
    gmt_create datetime null,
    constraint t_apply_id_uindex
        unique (id)
);

-- auto-generated definition
create table t_goods
(
    id        bigint auto_increment
        primary key,
    name      varchar(2000)                not null,
    gid       varchar(13)                  not null,
    price_in  decimal(10, 2) default 0.00  null,
    price_out decimal(10, 2) default 0.00  null,
    sid       bigint                       not null,
    deadline  date                         not null,
    num       bigint                       not null,
    pic_url   varchar(2000)                null,
    profit    decimal(10, 2) default 0.00  null,
    metrology varchar(10)                  null,
    type      varchar(30)    default '未分类' null,
    price_vip decimal(10, 2) default 0.00  null,
    constraint t_goods_id_uindex
        unique (id)
);

-- auto-generated definition
create table t_role
(
    id         bigint auto_increment comment 'id主键'
        primary key,
    user_id    bigint   null,
    shop_id    bigint   null,
    role       int      null comment '角色 0为普通用户 1为收银员 2为店主 3为超级管理员',
    gmt_create datetime null
);

-- auto-generated definition
create table t_shop
(
    id         bigint        not null comment 'id主键'
        primary key,
    shop_name  varchar(20)   null comment '店铺名',
    gmt_create datetime      null,
    gmt_update datetime      null,
    tip        text          null comment '介绍',
    status     int           null comment '店铺状态 0正常 1封禁 2注销',
    auth_zfb   text          null comment '支付宝付款商家授权码',
    auth_wx    text          null comment '微信付款商家授权码',
    zfb_status int default 0 null,
    wx_status  int default 0 null
);

-- auto-generated definition
create table t_shop_apply
(
    id          bigint auto_increment
        primary key,
    gmt_create  datetime      null,
    status      int           null,
    name        varchar(40)   null,
    descript    varchar(2000) null,
    img_shop    varchar(2000) null,
    img_idCard1 varchar(2000) null,
    img_idCard2 varchar(2000) null,
    code        varchar(20)   null,
    apply_id    bigint        null,
    tips        text          null,
    constraint t_shop_apply_id_uindex
        unique (id)
);

-- auto-generated definition
create table t_shop_category
(
    id       bigint auto_increment
        primary key,
    shop_id  bigint      null,
    category varchar(40) not null,
    constraint t_shop_category_id_uindex
        unique (id)
);

-- auto-generated definition
create table t_sys_menu
(
    menu_id   int auto_increment
        primary key,
    name      varchar(32)  null,
    path      varchar(32)  null,
    parent_id int          null,
    icon      varchar(32)  null,
    component varchar(300) null,
    hidden    int          null,
    sort      int          null,
    status    int          null,
    constraint t_sys_menu_menu_id_uindex
        unique (menu_id)
)
    comment '菜单';

-- auto-generated definition
create table t_sys_role_menu
(
    id      int auto_increment
        primary key,
    role_id int null,
    menu_id int null,
    type    int null,
    constraint t_sys_role_menu_id_uindex
        unique (id)
);

-- auto-generated definition
create table t_trade
(
    id          bigint         not null
        primary key,
    create_time datetime       not null,
    sid         bigint         not null,
    income      decimal(10, 2) not null,
    type        int            not null,
    remote_no   varchar(28)    null,
    status      int            null,
    msg         text           null,
    payer       text           null,
    operater    mediumtext     null,
    refund_no   int default 0  null,
    constraint t_trade_id_uindex
        unique (id)
);

-- auto-generated definition
create table t_trade_goods
(
    gid           varchar(13)                  not null,
    num           int                          not null,
    name          varchar(30)                  not null,
    trade_id      bigint                       not null,
    id            bigint auto_increment
        primary key,
    price         decimal(10, 2)               null,
    price_in      decimal(10, 2) default 0.00  null,
    type          varchar(20)    default '未分类' null,
    price_out_sum decimal(10, 2) default 0.00  null,
    price_in_sum  decimal(10, 2) default 0.00  null,
    constraint t_trade_goods_id_uindex
        unique (id)
);

-- auto-generated definition
create table t_trade_refund
(
    id          bigint auto_increment
        primary key,
    sid         bigint         null,
    money       decimal(10, 2) not null,
    msg         text           null,
    no          text           null,
    create_time datetime       null,
    status      int            null,
    operater    bigint         null,
    err_msg     text           null,
    type        int            null
);

-- auto-generated definition
create table t_user
(
    id            bigint                                                                                     not null comment 'id主键'
        primary key,
    user_name     varchar(40)                                                                                null comment '用户登录账户',
    user_pwd      varchar(32)                                                                                null comment '用户登录密码md5',
    user_nickname varchar(20)                                                                                null comment '用户名',
    email         varchar(40)                                                                                null comment '邮箱',
    gmt_create    datetime                                                                                   null,
    gmt_update    datetime                                                                                   null,
    status        int                                                                                        null comment '账户状态 0正常 1封禁 2注销',
    phone         varchar(11)                                                                                null comment '11位手机号',
    image         varchar(200) default 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png' null comment '头像链接'
);

-- auto-generated definition
create table t_vip
(
    id          bigint auto_increment
        primary key,
    shop_id     bigint        null,
    phone       varchar(11)   null,
    status      int           null,
    level       int default 0 null,
    gmt_limit   datetime      null,
    integration int default 0 null,
    sex         varchar(2)    null,
    age         varchar(10)   null,
    constraint t_vip_id_uindex
        unique (id)
);

INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (1, 1, 1000, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (2, 1, 10001, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (3, 2, 1000, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (4, 2, 10001, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (5, 3, 1000, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (6, 3, 10001, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (7, 1, 10002, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (8, 2, 10002, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (9, 3, 10002, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (10, 1, 1001, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (11, 2, 1001, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (12, 3, 1001, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (15, 2, 2000, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (16, 2, 2001, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (17, 2, 2002, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (19, 2, 2004, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (20, 3, 2000, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (21, 3, 2001, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (23, 3, 2004, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (24, 2, 20001, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (25, 2, 20011, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (26, 2, 20012, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (27, 2, 20021, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (30, 3, 20001, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (31, 3, 20011, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (32, 3, 20012, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (33, 3, 20021, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (34, 2, 20041, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (36, 2, 20043, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (37, 3, 20041, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (38, 2, 20002, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (39, 2, 20003, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (41, 3, 20003, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (42, 2, 20013, 2);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (43, 1, 10012, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (44, 2, 10012, 1);
INSERT INTO wisdomcashier.t_sys_role_menu (id, role_id, menu_id, type) VALUES (45, 3, 10012, 1);


INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (1000, '账号设置', 'userCenter', null, 'Avatar', null, 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (1001, '店铺相关', 'userCenter', null, 'ShoppingCartFull', null, 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (2000, '店铺功能', null, null, 'House', null, 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (2001, '商品管理', null, null, 'MessageBox', null, 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (2002, '运营数据', null, null, 'Document', null, 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (2003, '店铺管理', null, null, 'Shop', null, 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (2004, '会员管理', null, null, 'User', null, 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (10001, '我的信息', 'userCenter/myMessage', 1000, 'User', 'myMessage', 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (10002, '我的店铺', 'userCenter/myMessage', 1001, 'ShoppingBag', 'myShop', 0, 2, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (10012, '我要开店', null, 1001, 'DocumentAdd', 'CreateShop', 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20001, '收银', null, 2000, null, 'Charge', 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20002, '退款', null, 2000, null, 'Refund', 0, 2, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20003, '流水日志', null, 2000, null, 'TradeRecode', 0, 3, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20011, '商品列表', null, 2001, null, 'GoodsList', 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20012, '临期商品', null, 2001, null, 'Temporary', 0, 2, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20013, '分类管理', null, 2001, null, 'ClassificationManage', 0, 3, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20021, '交易数据', null, 2002, null, 'TradeDigital', 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20031, '店铺信息', null, 2003, null, 'ShopMessage', 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20032, '人员管理', null, 2003, null, 'PersonManage', 0, 1, 2);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20041, '会员详情', null, 2004, null, 'VipManage', 0, 1, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20042, '消息推送', null, 2004, null, 'MessagePush', 0, 3, 1);
INSERT INTO wisdomcashier.t_sys_menu (menu_id, name, path, parent_id, icon, component, hidden, sort, status) VALUES (20043, '商品销量', null, 2002, null, 'Volume', 0, 2, 1);

```