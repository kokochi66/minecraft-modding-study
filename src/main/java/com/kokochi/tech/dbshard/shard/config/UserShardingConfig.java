package com.kokochi.tech.dbshard.shard.config;

import com.kokochi.tech.dbshard.shard.enumType.ShardingTarget;
import com.kokochi.tech.dbshard.shard.property.ShardingProperty;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

//@Configuration
//@ConfigurationProperties(prefix = "sharding")
//@Setter
public class UserShardingConfig {

    private ShardingProperty user;

    @PostConstruct
    public void init() {
        System.out.println("TEST :: UserShardingConfig - init() :: 1");
        ShardingConfig.getShardingPropertyMap().put(ShardingTarget.USER, user);
    }
}
