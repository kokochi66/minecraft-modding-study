package com.kokochi.tech.dbshard.shard.aspect;

import com.kokochi.tech.dbshard.domain.shard.ShardKeyObject;
import com.kokochi.tech.dbshard.domain.shard.ShardNo;
import com.kokochi.tech.dbshard.domain.user.UserShardKey;
import com.kokochi.tech.dbshard.service.user.repository.UserShardKeyRepository;
import com.kokochi.tech.dbshard.shard.annotation.ShardService;
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
    }

    @Around("repositoryService() && @within(shardService) && args(shardKey,..)")
    public Object handler(ProceedingJoinPoint pjp, ShardService shardService, Object shardKey) throws Throwable {

        String paramShardKey = "";
        if (shardKey instanceof String) {
            paramShardKey = (String) shardKey;
        } else if (shardKey instanceof ShardKeyObject) {
            paramShardKey = ((ShardKeyObject) shardKey).getUserId();
        } else if (shardKey instanceof ShardNo) {
            Integer shardNo = ((ShardNo) shardKey).getShardNo();
            RoutingDataSourceManager.setCurrentDataSource(shardNo.longValue());
            return pjp.proceed();
        } else {
            throw new Exception("need shard key");
        }

        UserShardKey userShardKey = userShardKeyRepository.findByUserId(paramShardKey);
        RoutingDataSourceManager.setCurrentDataSource(userShardKey.getUserShardKeyId());
        return pjp.proceed();
    }

}
