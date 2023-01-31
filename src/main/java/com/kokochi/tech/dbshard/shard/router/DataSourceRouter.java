package com.kokochi.tech.dbshard.shard.router;

import com.kokochi.tech.dbshard.shard.property.ShardingProperty;
import com.kokochi.tech.dbshard.shard.thread.RoutingDataSourceManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceRouter extends AbstractRoutingDataSource {

    private Map<Integer, String> shards;

    private final String MASTER_DB = "db_shard_master";
    private final String SHARD_DB = "db_shard_shard";

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        shards = new HashMap<>();

        int shardNo = 0;
        for (Object item : targetDataSources.keySet()) {
            String dataSourceName = item.toString();
            shards.put(shardNo++, dataSourceName);
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getShard();
    }

    private String getShard() {
        Integer shardNo = RoutingDataSourceManager.getCurrentDataSource();
        if (shardNo == null) {
            shardNo = 0;
        }

        if (!shards.containsKey(shardNo)) {
            return shards.get(shards.keySet().stream().findAny().orElseThrow());
        }

        return shards.get(shardNo);
    }

//    private int getShardNo(UserHolder.Sharding sharding) {
//        System.out.println("TEST :: DataSourceRouter - getShardNo() :: 1");
//        if (sharding == null) {
//            return 0;
//        }
//
//        int shardNo = 0;
//        ShardingProperty shardingProperty = ShardingConfig.getShardingPropertyMap().get(sharding.getTarget());
//        if (shardingProperty.getStrategy() == ShardingStrategy.RANGE) {
//            shardNo = getShardNoByRange(shardingProperty.getRules(), sharding.getShardKey());
//        } else if (shardingProperty.getStrategy() == ShardingStrategy.MODULAR) {
//            shardNo = getShardNoByModular(shardingProperty.getMod(), sharding.getShardKey());
//        }
//        System.out.println("TEST :: DataSourceRouter - getShardNo() :: 2 :: shardNo = " + shardNo);
//        return shardNo;
//    }

    private int getShardNoByRange(List<ShardingProperty.ShardingRule> rules, long shardKey) {
        for (ShardingProperty.ShardingRule rule : rules) {
            if (rule.getRangeMin() <= shardKey && shardKey <= rule.getRangeMax()) {
                return rule.getShardNo();
            }
        }
        return 0;
    }

    private int getShardNoByModular(int modulus, long shardKey) {
        return (int) (shardKey % modulus);
    }

}
