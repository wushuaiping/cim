package com.crossoverjie.cim.server.test;

import com.crossoverjie.cim.client.CIMClientApplication;
import com.crossoverjie.cim.client.service.RouteRequest;
import com.crossoverjie.cim.client.vo.req.OnlineRequest;
import com.crossoverjie.cim.client.vo.res.CIMServerResVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/12/23 22:39
 * @since JDK 1.8
 */
@SpringBootTest(classes = CIMClientApplication.class)
@RunWith(SpringRunner.class)
public class RouteTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteTest.class);

    @Value("${cim.user.id}")
    private String userId;

    @Value("${cim.user.username}")
    private String username;

    @Autowired
    private RouteRequest routeRequest ;

    @Test
    public void test() throws Exception {
        OnlineRequest vo = new OnlineRequest(userId,username) ;
        CIMServerResVO.ServerInfo cimServer = routeRequest.getCIMServer(vo);
        LOGGER.info("cimServer=[{}]",cimServer.toString());
    }
}
