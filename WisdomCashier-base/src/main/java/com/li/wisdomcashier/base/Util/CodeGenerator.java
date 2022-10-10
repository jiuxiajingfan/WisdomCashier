package com.li.wisdomcashier.base.Util;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @ClassName CodeGenerator
 * @Description TODO
 * @Author lsw
 * @Date 2022/10/10 14:46
 * @Version 1.0
 */
public class CodeGenerator {
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        GlobalConfig gc = new GlobalConfig();
        String path = System.getProperty("user.dir");
        gc.setOutputDir(path+"/WisdomCashier-base/src/main/java");
        gc.setAuthor("lsw");
        gc.setOpen(false);
        gc.setFileOverride(false);
        //正则去除Service前的I
        gc.setServiceName("%sService");
        gc.setIdType(IdType.AUTO);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

//        数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://20.26.209.92:3307/edp?useUnicode=true&allowLoadLocalInfile=TRUE&characterEncoding=utf-8&rewriteBatchedStatements=true&autoReconnect=true&serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("onehrt");
        dsc.setPassword("OneHeart");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);


//        包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("base");
        pc.setParent("com.li.WisdomCashier");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

//        策略配置
        StrategyConfig sc = new StrategyConfig();
        //设置要映射的表
        sc.setInclude("taview_mcn_sms_limit_record");
        //@Table注解
        sc.setEntityTableFieldAnnotationEnable(true);
        //下划线转驼峰
        sc.setNaming(NamingStrategy.underline_to_camel);
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        //自动生成lombok
        sc.setEntityLombokModel(true);
//        //逻辑删除
//        sc.setLogicDeleteFieldName("字段名");

        //自动填充配置
        TableFill gmt_create = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill gmt_update = new TableFill("gmt_update", FieldFill.UPDATE);
        ArrayList<TableFill> objects = new ArrayList<>();
        objects.add(gmt_create);
        objects.add(gmt_update);
        sc.setTableFillList(objects);
        //乐观锁
        sc.setVersionFieldName("version");
        //驼峰命名
        sc.setRestControllerStyle(true);
        //Url 下划线命名
        sc.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(sc);

        mpg.execute();
    }
}
