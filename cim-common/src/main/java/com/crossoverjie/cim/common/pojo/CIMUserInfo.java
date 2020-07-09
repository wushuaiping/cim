package com.crossoverjie.cim.common.pojo;

/**
 * Function: 用户信息
 *
 * @author crossoverJie
 *         Date: 2018/12/24 02:33
 * @since JDK 1.8
 */
public class CIMUserInfo {
    private String userId ;
    private String username ;

    public CIMUserInfo(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
        return "CIMUserInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
