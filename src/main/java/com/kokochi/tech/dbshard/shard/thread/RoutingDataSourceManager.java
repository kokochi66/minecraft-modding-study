package com.kokochi.tech.dbshard.shard.thread;

import org.springframework.core.NamedThreadLocal;

public class RoutingDataSourceManager {

    private static final ThreadLocal<Integer> currentDataSourceName = new NamedThreadLocal<>("routing");

    private RoutingDataSourceManager() {
        throw new UnsupportedOperationException("this class can't be instance");
    }

    public static Integer getCurrentDataSource() {
        return currentDataSourceName.get();
    }

    public static void setCurrentDataSource(Long userShardKeyId) {
        currentDataSourceName.set((int) (userShardKeyId % 2));
    }

    public static void removeCurrentDataSource() {
        currentDataSourceName.remove();
    }
}
