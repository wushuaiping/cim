package com.crossoverjie.cim.route.service.impl;

import com.crossoverjie.cim.common.core.proxy.ProxyManager;
import com.crossoverjie.cim.common.enums.StatusEnum;
import com.crossoverjie.cim.common.exception.CIMException;
import com.crossoverjie.cim.common.pojo.CIMUserInfo;
import com.crossoverjie.cim.common.pojo.RouteInfo;
import com.crossoverjie.cim.common.route.algorithm.RouteHandle;
import com.crossoverjie.cim.common.util.RouteInfoParseUtil;
import com.crossoverjie.cim.route.api.vo.req.ChatReqVO;
import com.crossoverjie.cim.route.api.vo.req.OfflineRequest;
import com.crossoverjie.cim.route.api.vo.req.OnlineRequest;
import com.crossoverjie.cim.route.api.vo.res.CIMServerResVO;
import com.crossoverjie.cim.route.cache.ServerCache;
import com.crossoverjie.cim.route.service.AccountService;
import com.crossoverjie.cim.route.service.CommonBizService;
import com.crossoverjie.cim.route.service.UserInfoCacheService;
import com.crossoverjie.cim.server.api.ServerApi;
import com.crossoverjie.cim.server.api.vo.req.SendMsgReqVO;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.crossoverjie.cim.route.constant.Constants.Redis.ROUTE_PREFIX;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2018/12/23 21:58
 * @since JDK 1.8
 */
@Service
@AllArgsConstructor
public class AccountServiceRedisImpl implements AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceRedisImpl.class);

    private final RedisTemplate<String, String> redisTemplate;

    private final UserInfoCacheService userInfoCacheService;

    private final OkHttpClient okHttpClient;

    private final CommonBizService commonBizService;

    private final ServerCache serverCache;

    private final RouteHandle routeHandle;

    @Override
    public void loginServer(OnlineRequest loginReqVO) throws Exception {

        // 缓存用户
        userInfoCacheService.cacheUserInfo(loginReqVO);

        // 缓存用户到对应题组中
        userInfoCacheService.cacheTopicGroupUser(loginReqVO);
    }

    @Override
    public void saveRouteInfo(String userId, String server) throws Exception {
        String key = ROUTE_PREFIX + userId;
        redisTemplate.opsForValue().set(key, server);
    }

    @Override
    public Map<String, CIMServerResVO> loadRouteRelated() {

        Map<String, CIMServerResVO> routes = new HashMap<>(64);


        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions()
                .match(ROUTE_PREFIX + "*")
                .build();
        Cursor<byte[]> scan = connection.scan(options);

        while (scan.hasNext()) {
            byte[] next = scan.next();
            String key = new String(next, StandardCharsets.UTF_8);
            LOGGER.info("key={}", key);
            parseServerInfo(routes, key);

        }
        try {
            scan.close();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }

        return routes;
    }

    @Override
    public CIMServerResVO loadRouteRelatedByUserId(String userId) {
        String value = redisTemplate.opsForValue().get(ROUTE_PREFIX + userId);

        if (value == null) {
            throw new CIMException(StatusEnum.VALIDATION_FAIL, "用户已下线");
        }

        return new CIMServerResVO(RouteInfoParseUtil.parse(value));
    }

    private void parseServerInfo(Map<String, CIMServerResVO> routes, String key) {
        String userId = key.split(":")[1];
        String value = redisTemplate.opsForValue().get(key);
        CIMServerResVO cimServerResVO = new CIMServerResVO(RouteInfoParseUtil.parse(value));
        routes.put(userId, cimServerResVO);
    }


    @Override
    public void pushMsg(CIMServerResVO cimServerResVO, String sendUserId, ChatReqVO groupReqVO) throws Exception {
        CIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(sendUserId);

        String url = "http://" + cimServerResVO.getIp() + ":" + cimServerResVO.getHttpPort();
        ServerApi serverApi = new ProxyManager<>(ServerApi.class, url, okHttpClient).getInstance();
        SendMsgReqVO vo = new SendMsgReqVO(cimUserInfo.getUsername() + ":" + groupReqVO.getMsg(), groupReqVO.getUserId());
        Response response = null;
        try {
            response = (Response) serverApi.sendMsg(vo);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        } finally {
            response.body().close();
        }
    }

    @Override
    public void offline(OfflineRequest offlineRequest) throws Exception {

        //删除路由
        redisTemplate.delete(ROUTE_PREFIX + offlineRequest.getUserId());

        //删除登录状态
        userInfoCacheService.removeLoginStatus(offlineRequest.getTopicGroupId(), offlineRequest.getUserId());
    }

    @Override
    public Set<CIMUserInfo> searchUsersByTopicGroup(String topicGroupId) {
        return userInfoCacheService.onlineUserByTopicGroup(topicGroupId);
    }

    @Override
    public RouteInfo online(OnlineRequest loginReqVO) throws Exception {
        // 选择一台netty server
        String server = routeHandle.routeServer(serverCache.getServerList(), String.valueOf(loginReqVO.getUserId()));
        LOGGER.info("username=[{}] route server info=[{}]", loginReqVO.getUsername(), server);

        // 得到netty server解析成路由对象并检查netty server是否可用
        RouteInfo routeInfo = RouteInfoParseUtil.parse(server);
        commonBizService.checkServerAvailable(routeInfo);

        //登录到服务器
        loginServer(loginReqVO);

        //保存用户路由信息
        saveRouteInfo(loginReqVO.getUserId(), server);
        return routeInfo;
    }
}
