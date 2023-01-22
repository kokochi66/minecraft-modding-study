package com.kokochi.tech.dbshard.shard.property;

import com.kokochi.tech.dbshard.shard.enumType.ShardingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShardingProperty {

    private ShardingStrategy strategy;
    private List<ShardingRule> rules;
    private int mod;

    @Getter
    @Setter
    public static class ShardingRule {
        private int shardNo;
        private long rangeMin;
        private long rangeMax;
    }
}
