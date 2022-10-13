package com.li.wisdomcashier.base.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @ClassName MybatisPlusConfig
 * @Description TODO
 * @Author Nine
 * @Date 2022/10/11 11:32
 * @Version 1.0
 */
/**
 * 最新版 MybatisPlus 分页插件
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.li.wisdomcashier.base.mapper")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        /* 目前已有的功能；
            自动分页: PaginationInnerInterceptor
            多租户: TenantLineInnerInterceptor
            动态表名: DynamicTableNameInnerInterceptor
            乐观锁: OptimisticLockerInnerInterceptor
            sql 性能规范: IllegalSQLInnerInterceptor
            防止全表更新与删除: BlockAttackInnerInterceptor
        */

        // 防止全表更新与删除：攻击 SQL 阻断解析器、加入解析链，防止恶意进行 delete update 全表操作
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        // 自动分页，并指定数据库类型
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return interceptor;
    }

}

