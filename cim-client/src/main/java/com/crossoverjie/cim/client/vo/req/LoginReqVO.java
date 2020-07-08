package com.crossoverjie.cim.client.vo.req;

import com.crossoverjie.cim.common.req.BaseRequest;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/12/23 22:30
 * @since JDK 1.8
 */
public class LoginReqVO extends BaseRequest{
    private Long userId ;
    private String username ;

    public LoginReqVO(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

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

    @Override
    public String toString() {
        return "LoginReqVO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                "} " + super.toString();
    }
}
