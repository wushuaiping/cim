package com.crossoverjie.cim.route.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.crossoverjie.cim.common.pojo.CIMUserInfo;
import com.crossoverjie.cim.route.api.vo.req.OnlineRequest;
import com.crossoverjie.cim.route.service.UserInfoCacheService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.crossoverjie.cim.route.constant.Constants.Redis.LOGIN_PREFIX;
import static com.crossoverjie.cim.route.constant.Constants.Redis.TOPIC_GROUP_PREFIX;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2018/12/24 11:06
 * @since JDK 1.8
 */
@Service
@AllArgsConstructor
public class UserInfoCacheServiceImpl implements UserInfoCacheService {

    /**
     * todo 本地缓存，为了防止内存撑爆，后期可换为 LRU。
     */
    private final static Map<String, CIMUserInfo> USER_INFO_MAP = new ConcurrentHashMap<>(64);

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public CIMUserInfo loadUserInfoByUserId(String userId) {

        //优先从本地缓存获取
        CIMUserInfo cimUserInfo = USER_INFO_MAP.get(userId);
        if (cimUserInfo != null) {
            return cimUserInfo;
        }

        //load redis
        String userJson = redisTemplate.opsForValue().get(LOGIN_PREFIX + userId);
        if (StrUtil.isBlank(userJson)) {
            cimUserInfo = JSONObject.parseObject(userId, CIMUserInfo.class);
            USER_INFO_MAP.put(userId, cimUserInfo);
        }

        return cimUserInfo;
    }

    @Override
    public void removeLoginStatus(String topicGroupId, String userId) throws Exception {
        CIMUserInfo cimUserInfo = loadUserInfoByUserId(userId);
        if (Objects.nonNull(cimUserInfo)) {
            redisTemplate.opsForSet().remove(LOGIN_PREFIX + topicGroupId, JSONObject.toJSON(cimUserInfo));
        }
    }

    @Override
    public void cacheUserInfo(OnlineRequest loginReqVO) {
        String loginAccountKey = LOGIN_PREFIX + loginReqVO.getUserId();
        String value = redisTemplate.opsForValue().get(loginAccountKey);
        if (value == null) {
            redisTemplate.opsForValue().set(loginAccountKey, JSONObject.toJSONString(loginReqVO));
        }
    }

    @Override
    public void cacheTopicGroupUser(OnlineRequest loginReqVO) {
        // 把用户保存到所选题组中
        String topicGroupKey = TOPIC_GROUP_PREFIX + loginReqVO.getTopicGroupId();
        String json = toResponseJson(loginReqVO);
        Boolean isAbsent = redisTemplate.opsForSet().isMember(topicGroupKey, json);
        if (!isAbsent) {
            redisTemplate.opsForSet().add(topicGroupKey, json);
        }
    }

    @Override
    public Set<CIMUserInfo> onlineUserByTopicGroup(String topicGroupId) {
        Set<CIMUserInfo> set = null;
        Set<String> members = redisTemplate.opsForSet().members(TOPIC_GROUP_PREFIX + topicGroupId);
        for (String member : members) {
            if (set == null) {
                set = new HashSet<>(64);
            }
            CIMUserInfo cimUserInfo = loadUserInfoByUserId(member);
            set.add(cimUserInfo);
        }

        return set;
    }

    private String toResponseJson(OnlineRequest loginReqVO) {
        CIMUserInfo simpleUserResponse = new CIMUserInfo();
        simpleUserResponse.setLevel(loginReqVO.getLevel());
        simpleUserResponse.setUserId(loginReqVO.getUserId());
        simpleUserResponse.setUsername(loginReqVO.getUsername());
        simpleUserResponse.setTopicGroupId(loginReqVO.getTopicGroupId());
        return JSONObject.toJSONString(simpleUserResponse);
    }
}
