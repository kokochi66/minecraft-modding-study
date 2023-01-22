package com.kokochi.tech.dbshard.shard.router;

import com.kokochi.tech.dbshard.shard.config.ShardingConfig;
import com.kokochi.tech.dbshard.shard.enumType.ShardingStrategy;
import com.kokochi.tech.dbshard.shard.property.ShardingProperty;
import com.kokochi.tech.dbshard.shard.thread.UserHolder;
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
    private final String MASTER_DB = "master";
    private final String SLAVE_DB = "slave";

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        shards = new HashMap<>();

        for (Object item : targetDataSources.keySet()) {
            String dataSourceName = item.toString();
            String shardNoStr = dataSourceName.split(SHARD_DELIMITER)[0];

            MhaDataSource shard = getShard(shardNoStr);
            if (dataSourceName.contains(MASTER_DB)) {
                shard.setMasterName(dataSourceName);
            } else if (dataSourceName.contains(SLAVE_DB)) {
                shard.getSlaveName().add(dataSourceName);
            }
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        int shardNo = getShardNo(UserHolder.getSharding());
        MhaDataSource dataSource = shards.get(shardNo);
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? dataSource.getSlaveName().next() : dataSource.getMasterName();
    }

    private MhaDataSource getShard(String shardNoStr) {
        int shardNo = 0;
        if (StringUtils.isStrictlyNumeric(shardNoStr)) {
            shardNo = Integer.parseInt(shardNoStr);
        }

        MhaDataSource shard = shards.get(shardNo);
        if (shard == null) {
            shard = new MhaDataSource();
            shard.setSlaveName(new RoundRobin<>(new ArrayList<>()));
            shards.put(shardNo, shard);
        }
        return shard;
    }

    private int getShardNo(UserHolder.Sharding sharding) {
        if (sharding == null) {
            return 0;
        }

        int shardNo = 0;
        ShardingProperty shardingProperty = ShardingConfig.getShardingPropertyMap().get(sharding.getTarget());
        if (shardingProperty.getStrategy() == ShardingStrategy.RANGE) {
            shardNo = getShardNoByRange(shardingProperty.getRules(), sharding.getShardKey());
        } else if (shardingProperty.getStrategy() == ShardingStrategy.MODULAR) {
            shardNo = getShardNoByModular(shardingProperty.getMod(), sharding.getShardKey());
        }
        return shardNo;
    }

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


    @Getter
    @Setter
    public class MhaDataSource {
        private String masterName;
        private RoundRobin<String> slaveName;
    }
}
