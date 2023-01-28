package com.kokochi.tech.dbshard.shard.aspect;

import com.kokochi.tech.dbshard.shard.annotation.ShardingRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

//
//@Component
//@Aspect
//@RequiredArgsConstructor
public class RepositoryServiceAspect {

    @Pointcut("execution(public * com.kokochi.tech.dbshard.service..*.*(..))")
    private void repositoryService() {
        System.out.println("TEST :: RepositoryServiceAspect - repositoryService :: 1");
    }

    @Around("repositoryService() && @within(sharding) && args(shardKey,..)")
    public Object handler(ProceedingJoinPoint pjp, ShardingRepository sharding, long shardKey) throws Throwable {
        System.out.println("TEST :: RepositoryServiceAspect - handler :: 1");
//        UserHolder.setSharding(sharding.target(), shardKey);
        Object returnVal = pjp.proceed();
//        UserHolder.clearSharding();
        return returnVal;
    }

}
