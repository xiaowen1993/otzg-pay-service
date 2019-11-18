package com.bcb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = {"com.bcb"},
    repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class
)


/**
 * 打包发布到tomcat目录下的时候
 * 1、需要jre8.0以上，Tomcat8.5支持
 * 2、WebSocketConfig 配置需要注释掉
 * @author 曹操,caocao
 */

//扫描servlet组件
@ServletComponentScan
//开启事务支持后，然后在访问数据库的Service方法上添加注解 @Transactional
@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties
public class MainApplication{
    public static void main(String [] args){
        SpringApplication.run(MainApplication.class,args);
    }
}
