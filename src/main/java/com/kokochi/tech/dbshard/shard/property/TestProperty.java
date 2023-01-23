package com.kokochi.tech.dbshard.shard.property;

import com.kokochi.tech.dbshard.shard.enumType.ShardingStrategy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "test.property")
public class TestProperty {

    private String name;
    private String password;
    private List<Streamer> list;

    @Getter
    @Setter
    public static class Streamer {
        private String name;
        private String id;
    }
}
