package com.kokochi.tech.dbshard.shard.custom.config;

import com.kokochi.tech.dbshard.shard.annotation.ShardingRepository;
import com.kokochi.tech.dbshard.shard.property.ShardingDataSourceProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.kokochi.tech.dbshard.service",
        entityManagerFactoryRef = "masterEntityManager",
        transactionManagerRef = "masterTransactionManager",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {ShardingRepository.class}
        )
)
public class MasterDatabaseConfig {

    @Autowired
    private ShardingDataSourceProperty property;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean masterEntityManager() {
        System.out.println("TEST :: MasterDatabaseConfig :: masterEntityManager : 1");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(masterDataSource());

        em.setPackagesToScan("com.kokochi.tech.dbshard.domain");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(property.getShowSql());
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", property.getDdlAuto());
        properties.put("hibernate.physical_naming_strategy", property.getNaming());
//        properties.put("hibernate.dialect", "update");
        em.setJpaPropertyMap(properties);
        System.out.println("TEST :: MasterDatabaseConfig :: masterEntityManager : 2");
        return em;
    }

    @Bean
    @Primary
    public DataSource masterDataSource() {
        System.out.println("TEST :: MasterDatabaseConfig :: masterDataSource : 1");
        return DataSourceBuilder.create()
                .username(property.getUsername())
                .password(property.getPassword())
                .url(property.getMaster().getUrl())
                .driverClassName(property.getDriver())
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager masterTransactionManager() {
        System.out.println("TEST :: MasterDatabaseConfig :: masterTransactionManager : 1");
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(masterEntityManager().getObject());
        return transactionManager;
    }

}
