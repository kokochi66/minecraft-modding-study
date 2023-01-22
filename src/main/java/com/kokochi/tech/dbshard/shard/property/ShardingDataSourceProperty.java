package com.kokochi.tech.dbshard.shard.property;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShardingDataSourceProperty {

    private List<Shard> shards;

    @Getter
    @Setter
    public static class Shard {
        private String username;
        private String password;
        private Property master;
        private List<Property> slaves;
    }

    @Getter
    @Setter
    public static class Property {
        private String name;
        private String url;
    }
}
