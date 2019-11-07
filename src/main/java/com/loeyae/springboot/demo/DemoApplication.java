package com.loeyae.springboot.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
//没有连接数据库的时候报错  需要加上这一句
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class DemoApplication {


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.loeyae.springboot.*.mapper");
        return mapperScannerConfigurer;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
