package com.kokochi.tech.dbshard.shard.router;

import com.kokochi.tech.dbshard.shard.property.ShardingProperty;
import com.mysql.cj.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceRouter extends AbstractRoutingDataSource {

    private Map<Integer, MhaDataSource> shards;

    private final String SHARD_DELIMITER = ",";
    private final String MASTER_DB = "db_shard_master";
    private final String SHARD_DB = "db_shard_shard";

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        System.out.println("TEST :: DataSourceRouter - setTargetDataSources() :: 1");
        super.setTargetDataSources(targetDataSources);

        shards = new HashMap<>();

        for (Object item : targetDataSources.keySet()) {
            String dataSourceName = item.toString();
            String shardNoStr = dataSourceName.split(SHARD_DELIMITER)[0];

            MhaDataSource shard = getShard(shardNoStr);
            if (dataSourceName.contains(MASTER_DB)) {
                shard.setMasterName(dataSourceName);
            } else if (dataSourceName.contains(SHARD_DB)) {
                shard.getShardName().add(dataSourceName);
            }
        }
        System.out.println("TEST :: DataSourceRouter - setTargetDataSources() :: 2");
    }

    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println("TEST :: DataSourceRouter - determineCurrentLookupKey() :: 1");
        int shardNo = 0;
        MhaDataSource dataSource = shards.get(shardNo);
        System.out.println("TEST :: DataSourceRouter - determineCurrentLookupKey() :: 2 :: dataSource MasterName = " + dataSource.getMasterName());
        dataSource.getShardName().getList().forEach(s -> {
            System.out.println("TEST :: DataSourceRouter - determineCurrentLookupKey() :: 2 :: dataSource slaveNames = " + s);
        });
        System.out.println("TEST :: DataSourceRouter - determineCurrentLookupKey() :: 3 :: TransactionReadOnly :: " + TransactionSynchronizationManager.isCurrentTransactionReadOnly());
        System.out.println("TEST :: DataSourceRouter - determineCurrentLookupKey() :: 3 :: getCurrentTransactionName :: " + TransactionSynchronizationManager.getCurrentTransactionName());
        Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();

        for (Object o : resourceMap.keySet()) {
            System.out.println("TEST :: DataSourceRouter - determineCurrentLookupKey() :: 4 :: resource = " + o);
        }

        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? dataSource.getShardName().next() : dataSource.getMasterName();
    }

    private MhaDataSource getShard(String shardNoStr) {
        System.out.println("TEST :: DataSourceRouter - getShard() :: 1");
        int shardNo = 0;
        if (StringUtils.isStrictlyNumeric(shardNoStr)) {
            shardNo = Integer.parseInt(shardNoStr);
        }
        System.out.println("TEST :: DataSourceRouter - getShard() :: 2");
        MhaDataSource shard = shards.get(shardNo);
        if (shard == null) {
            shard = new MhaDataSource();
            shard.setShardName(new RoundRobin<>(new ArrayList<>()));
            shards.put(shardNo, shard);
        }
        System.out.println("TEST :: DataSourceRouter - getShard() :: 3");
        return shard;
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
        System.out.println("TEST :: DataSourceRouter - getShardNoByRange() :: 1");
        for (ShardingProperty.ShardingRule rule : rules) {
            if (rule.getRangeMin() <= shardKey && shardKey <= rule.getRangeMax()) {
                return rule.getShardNo();
            }
        }
        return 0;
    }

    private int getShardNoByModular(int modulus, long shardKey) {
        System.out.println("TEST :: DataSourceRouter - getShardNoByModular() :: 1");
        return (int) (shardKey % modulus);
    }


    @Getter
    @Setter
    public class MhaDataSource {
        private String masterName;
        private RoundRobin<String> shardName;
    }
}
