package com.kokochi.tech.dbshard.shard;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.product.ProductService;
import com.kokochi.tech.dbshard.service.user.UserService;
import com.kokochi.tech.dbshard.shard.property.TestProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootTest
public class ShardingTest {

    @Autowired private UserService userService;
    @Autowired private ProductService productService;
    @Autowired private TestProperty testProperty;

    @Autowired private PlatformTransactionManager transactionManager;

    @Test
    void 테스트() {
        User testUser = User.createUser("testUser", "1234");
        userService.upsertUser(testUser);


//        Product product = Product.createProduct(ProductType.ANIME, ProductSeasonType.FALL_20, "testTitle", "testDirector");
//        productService.upsertProduct(product);

        System.out.println("TEST :: confirm");

    }

}
