package com.crossoverjie.cim.route.api.vo.req;

import com.crossoverjie.cim.common.req.BaseRequest;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2018/12/23 22:30
 * @since JDK 1.8
 */
public class LoginReqVO extends BaseRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 需要进行比赛的题组id
     */
    private String topicGroupId;

    /**
     * 用户名
     */
    private String username;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTopicGroupId() {
        return topicGroupId;
    }

    public void setTopicGroupId(String topicGroupId) {
        this.topicGroupId = topicGroupId;
    }

    @Override
    public String toString() {
        return "LoginReqVO{" +
                "userId=" + userId +
                ", topicGroupId='" + topicGroupId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
