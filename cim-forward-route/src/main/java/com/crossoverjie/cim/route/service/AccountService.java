package com.crossoverjie.cim.route.service;

import com.crossoverjie.cim.common.pojo.CIMUserInfo;
import com.crossoverjie.cim.common.pojo.RouteInfo;
import com.crossoverjie.cim.route.api.vo.req.ChatReqVO;
import com.crossoverjie.cim.route.api.vo.req.OfflineRequest;
import com.crossoverjie.cim.route.api.vo.req.OnlineRequest;
import com.crossoverjie.cim.route.api.vo.res.CIMServerResVO;

import java.util.Map;
import java.util.Set;

/**
 * Function: 账户服务
 *
 * @author crossoverJie
 * Date: 2018/12/23 21:57
 * @since JDK 1.8
 */
public interface AccountService {

    /**
     * 登录服务
     *
     * @param loginReqVO 登录信息
     * @throws Exception
     */
    void loginServer(OnlineRequest loginReqVO) throws Exception;

    /**
     * 保存路由信息
     *
     * @param server     服务器信息
     * @param loginReqVO 用户信息
     * @throws Exception
     */
    void saveRouteInfo(String userId, String server) throws Exception;

    /**
     * 加载所有用户的路有关系
     *
     * @return 所有的路由关系
     */
    Map<String, CIMServerResVO> loadRouteRelated();

    /**
     * 获取某个用户的路有关系
     *
     * @param userId
     * @return 获取某个用户的路有关系
     */
    CIMServerResVO loadRouteRelatedByUserId(String userId);

    /**
     * 推送消息
     *
     * @param cimServerResVO
     * @param groupReqVO     消息
     * @param sendUserId     发送者的ID
     * @throws Exception
     */
    void pushMsg(CIMServerResVO cimServerResVO, String sendUserId, ChatReqVO groupReqVO) throws Exception;

    /**
     * 用户下线
     *
     * @param userId 下线用户ID
     * @throws Exception
     */
    void offline(OfflineRequest offlineRequest) throws Exception;

    Set<CIMUserInfo> searchUsersByTopicGroup(String topicGroupId);

    RouteInfo online(OnlineRequest loginReqVO) throws Exception;
}
