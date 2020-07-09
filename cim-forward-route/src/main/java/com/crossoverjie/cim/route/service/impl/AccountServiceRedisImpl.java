package com.crossoverjie.cim.route.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.crossoverjie.cim.common.core.proxy.ProxyManager;
import com.crossoverjie.cim.common.enums.StatusEnum;
import com.crossoverjie.cim.common.exception.CIMException;
import com.crossoverjie.cim.common.pojo.CIMUserInfo;
import com.crossoverjie.cim.common.util.RouteInfoParseUtil;
import com.crossoverjie.cim.route.api.vo.req.ChatReqVO;
import com.crossoverjie.cim.route.api.vo.req.LoginReqVO;
import com.crossoverjie.cim.route.api.vo.res.CIMServerResVO;
import com.crossoverjie.cim.route.api.vo.res.RegisterInfoResVO;
import com.crossoverjie.cim.route.api.vo.res.SimpleUserResponse;
import com.crossoverjie.cim.route.service.AccountService;
import com.crossoverjie.cim.route.service.UserInfoCacheService;
import com.crossoverjie.cim.server.api.ServerApi;
import com.crossoverjie.cim.server.api.vo.req.SendMsgReqVO;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.crossoverjie.cim.route.constant.Constants.Redis.*;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2018/12/23 21:58
 * @since JDK 1.8
 */
@Service
public class AccountServiceRedisImpl implements AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceRedisImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public RegisterInfoResVO register(RegisterInfoResVO info) {
        String key = ACCOUNT_PREFIX + info.getUserId();

        String name = redisTemplate.opsForValue().get(info.getUsername());
        if (null == name) {
            //为了方便查询，冗余一份
            redisTemplate.opsForValue().set(key, info.getUsername());
            redisTemplate.opsForValue().set(info.getUsername(), key);
        } else {
            String userId = name.split(":")[1];
            info.setUserId(userId);
            info.setUsername(info.getUsername());
        }

        return info;
    }

    @Override
    public void loginServer(LoginReqVO loginReqVO) throws Exception {

        // 缓存登陆服务器的用户
        cacheLoginUser(loginReqVO);

        // 缓存用户到对应题组中
        cacheTopicGroupUser(loginReqVO);
    }

    private void cacheTopicGroupUser(LoginReqVO loginReqVO) {
        // 把用户保存到所选题组中
        String topicGroupKey = TOPIC_GROUP_PREFIX + loginReqVO.getTopicGroupId();
        String json = toResponseJson(loginReqVO);
        Boolean isAbsent = redisTemplate.opsForSet().isMember(topicGroupKey, json);
        if (!isAbsent) {
            redisTemplate.opsForSet().add(topicGroupKey, json);
        }
    }

    private String toResponseJson(LoginReqVO loginReqVO) {
        SimpleUserResponse simpleUserResponse = new SimpleUserResponse();
        simpleUserResponse.setLevel(loginReqVO.getLevel());
        simpleUserResponse.setUserId(loginReqVO.getUserId());
        simpleUserResponse.setUsername(loginReqVO.getUsername());
        return JSONObject.toJSONString(simpleUserResponse);
    }

    private void cacheLoginUser(LoginReqVO loginReqVO) {
        String loginAccountKey = ACCOUNT_PREFIX + loginReqVO.getUserId();
        String value = redisTemplate.opsForValue().get(loginAccountKey);
        if (value == null) {
            redisTemplate.opsForValue().set(loginAccountKey, JSONObject.toJSONString(loginReqVO));
        }
    }

    @Override
    public void saveRouteInfo(LoginReqVO loginReqVO, String server) throws Exception {
        String key = ROUTE_PREFIX + loginReqVO.getUserId();
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
    public void offLine(String userId) throws Exception {

        // TODO: 2019-01-21 改为一个原子命令，以防数据一致性

        //删除路由
        redisTemplate.delete(ROUTE_PREFIX + userId);

        //删除登录状态
        userInfoCacheService.removeLoginStatus(userId);
    }

    @Override
    public List<SimpleUserResponse> searchUsersByTopicGroup(String topicGroupId) {
        String key = TOPIC_GROUP_PREFIX + topicGroupId;
        Set<String> onlineUsers = redisTemplate.opsForSet().members(key);
        List<SimpleUserResponse> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(onlineUsers)) {
            onlineUsers.forEach(s -> result.add(JSONObject.parseObject(s, SimpleUserResponse.class)));
        }
        return result;
    }
}
