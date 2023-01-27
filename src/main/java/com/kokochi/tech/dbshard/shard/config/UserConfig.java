package com.kokochi.tech.dbshard.shard.config;

import com.kokochi.tech.dbshard.shard.property.ShardingDataSourceProperty;
import com.kokochi.tech.dbshard.shard.router.DataSourceRouter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

@Configuration
@EnableJpaRepositories
public class UserConfig {

    @Autowired
    private ShardingDataSourceProperty user;
    private final String SHARD_DELIMITER = "_";

    @Bean
    public DataSource userDataSource() {
        DataSourceRouter router = new DataSourceRouter();
        Map<Object, Object> datasourceMap = new LinkedHashMap<>();
        
        for (int i= 0;i< user.getShards().size(); i++) {
            ShardingDataSourceProperty.Shard shard = user.getShards().get(i);

            DataSource masterDs = datasource(shard.getUsername(), shard.getPassword(), shard.getMaster().getUrl());
            datasourceMap.put(i + SHARD_DELIMITER + shard.getMaster().getName(), masterDs);

            for (ShardingDataSourceProperty.Property slave : shard.getSlaves()) {
                DataSource slaveDs = datasource(shard.getUsername(), shard.getPassword(), slave.getUrl());
                datasourceMap.put(i + SHARD_DELIMITER + slave.getName(), slaveDs);


            }
        }

        router.setTargetDataSources(datasourceMap);
        router.afterPropertiesSet();
        return new LazyConnectionDataSourceProxy(router);
    }

    private DataSource datasource(String username, String password, String url) {
        return new UserDataSource(username, password, url);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class UserDataSource implements DataSource {

        private String userName;
        private String password;
        private String url;

        @Override
        public Connection getConnection() throws SQLException {
            return null;
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return null;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }
    }
}
