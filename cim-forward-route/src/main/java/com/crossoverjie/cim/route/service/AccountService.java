package com.crossoverjie.cim.route.service;

import com.crossoverjie.cim.route.api.vo.req.ChatReqVO;
import com.crossoverjie.cim.route.api.vo.req.LoginReqVO;
import com.crossoverjie.cim.route.api.vo.res.CIMServerResVO;
import com.crossoverjie.cim.route.api.vo.res.RegisterInfoResVO;
import com.crossoverjie.cim.route.api.vo.res.SimpleUserResponse;

import java.util.List;
import java.util.Map;

/**
 * Function: 账户服务
 *
 * @author crossoverJie
 * Date: 2018/12/23 21:57
 * @since JDK 1.8
 */
public interface AccountService {

    /**
     * 注册用户
     *
     * @param info 用户信息
     * @return
     * @throws Exception
     */
    RegisterInfoResVO register(RegisterInfoResVO info) throws Exception;

    /**
     * 登录服务
     *
     * @param loginReqVO 登录信息
     * @throws Exception
     */
    void loginServer(LoginReqVO loginReqVO) throws Exception;

    /**
     * 保存路由信息
     *
     * @param server     服务器信息
     * @param loginReqVO 用户信息
     * @throws Exception
     */
    void saveRouteInfo(LoginReqVO loginReqVO, String server) throws Exception;

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
    void offLine(String userId) throws Exception;

    List<SimpleUserResponse> searchUsersByTopicGroup(String topicGroupId);
}
