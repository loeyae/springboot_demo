package com.loeyae.springboot.demo.tool;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * MysqlGenerator.
 *
 * @date: 2019-10-24
 * @version: 1.0
 * @author: zhangyi07@beyondsoft.com
 */
public class MysqlGenerator {
    /**
     * 数据库配置
     */
    private static String dbUrl = "jdbc:mysql://localhost:3306/fnb_app?useUnicode=true" +
            "&useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static String dbUsername = "doctrine";
    private static String dbPassword = "doctrine";
    /**
     * 模块名
     */
    private static String moduleName = "demo";
    /**
     * 表名
     */
    private static String[] tableNames = {"application"};

    /**
     * RUN THIS
     */
    public static void main(String[] args) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("zhang yi");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dbUrl);
        dsc.setDriverName(dbDriver);
        dsc.setUsername(dbUsername);
        dsc.setPassword(dbPassword);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(moduleName);
        pc.setParent("com.loeyae.springboot");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/base_tools/common_coder/src/main/resources/mapper/" + pc.getModuleName() + "/" +
                        tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
//        strategy.setSuperEntityClass("com.bys.rdc.common.mybatis.SuperEntity");
//        strategy.setSuperEntityColumns("create_time, update_time");
//        strategy.setSuperMapperClass("com.bys.rdc.common.mybatis.SuperMapper");
//        strategy.setSuperServiceClass("com.bys.rdc.common.mybatis.SuperService");
//        strategy.setSuperServiceImplClass("com.bys.rdc.common.mybatis.SuperServiceImpl");
//        strategy.setSuperControllerClass("com.bys.rdc.common.mybatis.SuperController");
        strategy.setInclude(tableNames);
//        strategy.setSuperEntityColumns("create_time", "update_time");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}