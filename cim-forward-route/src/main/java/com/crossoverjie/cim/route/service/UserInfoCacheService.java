package com.crossoverjie.cim.route.service;

import com.crossoverjie.cim.common.pojo.CIMUserInfo;
import com.crossoverjie.cim.route.api.vo.req.OnlineRequest;

import java.util.Set;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2018/12/24 11:06
 * @since JDK 1.8
 */
public interface UserInfoCacheService {

    /**
     * 通过 userID 获取用户信息
     *
     * @param userId 用户唯一 ID
     * @return
     * @throws Exception
     */
    CIMUserInfo loadUserInfoByUserId(String userId);

    /**
     * 清除用户的登录状态
     *
     * @param userId
     * @throws Exception
     */
    void removeLoginStatus(String topicGroupId, String userId) throws Exception;

    /**
     * 缓存当前登陆用户
     *
     * @param loginReqVO
     */
    void cacheUserInfo(OnlineRequest loginReqVO);

    /**
     * 缓存当前登陆用户所在题组
     *
     * @param loginReqVO
     */
    void cacheTopicGroupUser(OnlineRequest loginReqVO);

    /**
     * query all online user
     *
     * @return online user
     */
    Set<CIMUserInfo> onlineUserByTopicGroup(String topicGroupId);
}
