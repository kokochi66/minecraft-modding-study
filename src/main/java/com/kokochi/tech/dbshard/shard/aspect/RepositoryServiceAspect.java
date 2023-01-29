package com.kokochi.tech.dbshard.shard.aspect;

import com.kokochi.tech.dbshard.domain.shard.ShardKey;
import com.kokochi.tech.dbshard.domain.user.UserShardKey;
import com.kokochi.tech.dbshard.service.user.repository.UserShardKeyRepository;
import com.kokochi.tech.dbshard.shard.annotation.ShardService;
import com.kokochi.tech.dbshard.shard.annotation.ShardingRepository;
import com.kokochi.tech.dbshard.shard.thread.RoutingDataSourceManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Aspect
@RequiredArgsConstructor
public class RepositoryServiceAspect {

    @Autowired
    private UserShardKeyRepository userShardKeyRepository;

    @Pointcut("execution(public * com.kokochi.tech.dbshard.service..*.*(..))")
    private void repositoryService() {
        System.out.println("TEST :: RepositoryServiceAspect - repositoryService :: 1");
    }

    @Around("repositoryService() && @within(shardService) && args(shardKey,..)")
    public Object handler(ProceedingJoinPoint pjp, ShardService shardService, ShardKey shardKey) throws Throwable {
        System.out.println("TEST :: RepositoryServiceAspect - handler :: 1");

        if (shardKey.getUserId() == null) {
            throw new Exception("need shard key");
        }

        UserShardKey userShardKey = userShardKeyRepository.findByUserId(shardKey.getUserId());

        RoutingDataSourceManager.setCurrentDataSource(userShardKey.getUserShardKeyId());
        Object returnVal = pjp.proceed();
        return returnVal;
    }

}
