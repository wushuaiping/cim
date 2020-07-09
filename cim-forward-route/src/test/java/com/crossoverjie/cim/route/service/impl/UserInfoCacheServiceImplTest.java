package com.crossoverjie.cim.route.service.impl;

import com.crossoverjie.cim.route.RouteApplication;
import com.crossoverjie.cim.route.service.UserInfoCacheService;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RouteApplication.class)
@RunWith(SpringRunner.class)
public class UserInfoCacheServiceImplTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserInfoCacheServiceImplTest.class);


    @Autowired
    private UserInfoCacheService userInfoCacheService;


}