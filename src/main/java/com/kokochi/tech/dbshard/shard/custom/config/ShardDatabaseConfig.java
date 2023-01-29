package com.kokochi.tech.dbshard.shard.custom.config;

import com.kokochi.tech.dbshard.shard.annotation.MasterRepository;
import com.kokochi.tech.dbshard.shard.property.ShardingDataSourceProperty;
import com.kokochi.tech.dbshard.shard.router.DataSourceRouter;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.kokochi.tech.dbshard.service",
        entityManagerFactoryRef = "shardEntityManager",
        transactionManagerRef = "shardTransactionManager",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {MasterRepository.class}
        )
)
public class ShardDatabaseConfig {

    @Autowired
    private ShardingDataSourceProperty property;

    @Bean
    public LocalContainerEntityManagerFactoryBean shardEntityManager() {
        System.out.println("TEST :: ShardDatabaseConfig :: shardEntityManager : 1");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(shardDataSource());
        em.setPackagesToScan("com.kokochi.tech.dbshard.domain");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(property.getShowSql());
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", property.getDdlAuto());
        properties.put("hibernate.physical_naming_strategy", property.getNaming());
//        properties.put("hibernate.dialect", "update");
        em.setJpaPropertyMap(properties);
        System.out.println("TEST :: ShardDatabaseConfig :: shardEntityManager : 2");
        return em;
    }

    @Bean(name = "shardDataSource")
    public DataSource shardDataSource() {
        System.out.println("TEST :: ShardDatabaseConfig :: shard1DataSource : 1");
        DataSourceRouter router = new DataSourceRouter();
        Map<Object, Object> datasourceMap = new LinkedHashMap<>();

        for (ShardingDataSourceProperty.Property shard : property.getShards()) {
            DataSource shardDs = datasource(shard.getUrl());
            datasourceMap.put(shard.getName(), shardDs);
        }
        System.out.println("TEST :: ShardDatabaseConfig - shard1DataSource() :: 2");

        router.setTargetDataSources(datasourceMap);
        router.afterPropertiesSet();
        System.out.println("TEST :: ShardDatabaseConfig - shard1DataSource() :: 3");
        return new LazyConnectionDataSourceProxy(router);


    }

    private DataSource datasource(String url) {
        return DataSourceBuilder.create()
                .username(property.getUsername())
                .password(property.getPassword())
                .url(url)
                .driverClassName(property.getDriver())
                .build();
    }

//    @Bean(name = "shard2DataSource")
//    public DataSource shard2DataSource() {
//        System.out.println("TEST :: ShardDatabaseConfig :: shard2DataSource : 1");
//        return DataSourceBuilder.create()
//                .username(property.getUsername())
//                .password(property.getPassword())
//                .url(property.getShards().get(1).getUrl())
//                .driverClassName(property.getDriver())
//                .build();
//    }

    @Bean
    public PlatformTransactionManager shardTransactionManager() {
        System.out.println("TEST :: ShardDatabaseConfig :: shardTransactionManager : 1");
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(shardEntityManager().getObject());
        return transactionManager;
    }

}
