package com.kokochi.tech.dbshard.shard.config;

import com.kokochi.tech.dbshard.shard.property.ShardingDataSourceProperty;
import com.kokochi.tech.dbshard.shard.router.DataSourceRouter;
import com.mysql.cj.conf.DatabaseUrlContainer;
import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

//@Component
@Configuration
@EnableJpaRepositories(basePackages = "com.kokochi.tech.dbshard.service")
public class UserConfig {

    @Autowired
    private ShardingDataSourceProperty user;

    private final String SHARD_DELIMITER = ",";

    @Bean
    public DataSource userDataSource() {
        System.out.println("TEST :: UserConfig - userDataSource() :: 1");
        DataSourceRouter router = new DataSourceRouter();
        Map<Object, Object> datasourceMap = new LinkedHashMap<>();

        DataSource masterDs = datasource(user.getUsername(), user.getPassword(), user.getMaster().getUrl());
        datasourceMap.put(user.getMaster().getName(), masterDs);
        for (ShardingDataSourceProperty.Property shard : user.getShards()) {
            DataSource slaveDs = datasource(user.getUsername(), user.getPassword(), shard.getUrl());
            datasourceMap.put(shard.getName(), slaveDs);
        }
        System.out.println("TEST :: UserConfig - userDataSource() :: 2");

        router.setTargetDataSources(datasourceMap);
        router.afterPropertiesSet();
        System.out.println("TEST :: UserConfig - userDataSource() :: 3");
        return new LazyConnectionDataSourceProxy(router);
    }

    private DataSource datasource(String username, String password, String url) {
        System.out.println("TEST :: UserConfig - datasource() :: 1 = " + username +" " + password +" " + url);
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setURL(url);
        return dataSource;
    }
}
