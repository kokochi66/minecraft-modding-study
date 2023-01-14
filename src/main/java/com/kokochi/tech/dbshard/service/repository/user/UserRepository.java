package com.kokochi.tech.dbshard.service.repository.user;

import com.kokochi.tech.dbshard.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
