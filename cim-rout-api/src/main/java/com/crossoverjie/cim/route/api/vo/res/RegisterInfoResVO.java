package com.crossoverjie.cim.route.api.vo.res;

import java.io.Serializable;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/12/23 21:54
 * @since JDK 1.8
 */
public class RegisterInfoResVO implements Serializable{
    private Long userId ;
    private String username ;

    public RegisterInfoResVO(Long userId, String username) {
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
        return "RegisterInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
