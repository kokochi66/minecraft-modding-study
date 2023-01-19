package com.kokochi.tech.dbshard.service.user.repository;

import com.kokochi.tech.dbshard.domain.user.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSearchHistoryRepository extends JpaRepository<UserHistory, Long> {

}
