package com.kokochi.tech.dbshard.shard.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kokochi.tech.dbshard.shard.annotation.Sharding;
import com.kokochi.tech.dbshard.shard.thread.UserHolder;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class RepositoryServiceAspect {


    @Pointcut("execution(public * com.kokochi.tech.dbshard.service..*.*(..))")
    private void repositoryService() {

    }

    @Around("repositoryService() && @within(sharding) && args(shardKey,..)")
    public Object handler(ProceedingJoinPoint pjp, Sharding sharding, long shardKey) throws Throwable {
        System.out.println("TEST :: RepositoryServiceAspect - handler :: 1");
        UserHolder.setSharding(sharding.target(), shardKey);
        Object returnVal = pjp.proceed();
        UserHolder.clearSharding();
        return returnVal;
    }

}
