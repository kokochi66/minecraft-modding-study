package com.kokochi.tech.dbshard.shard.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "datasource.user")
public class ShardingDataSourceProperty {

    private String username;
    private String password;
    private String driver;
    private Boolean showSql;
    private String ddlAuto;
    private String naming;


    private Property master;
    private List<Property> shards;

    @Getter
    @Setter
    public static class Property {
        private String name;
        private String url;
    }
}
