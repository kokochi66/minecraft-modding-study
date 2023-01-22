package com.kokochi.tech.dbshard.shard.annotation;

import com.kokochi.tech.dbshard.shard.enumType.ShardingTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sharding {
    ShardingTarget target();
}
