package com.kokochi.tech.dbshard.shard.config;

import com.kokochi.tech.dbshard.shard.enumType.ShardingTarget;
import com.kokochi.tech.dbshard.shard.property.ShardingProperty;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
public class ShardingConfig {
    private static Map<ShardingTarget, ShardingProperty> shardingPropertyMap = new ConcurrentHashMap<>();

    public static Map<ShardingTarget, ShardingProperty> getShardingPropertyMap() {
        System.out.println("TEST :: ShardingConfig - getShardingPropertyMap() :: 1");
        return shardingPropertyMap;
    }
}
