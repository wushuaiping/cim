package com.crossoverjie.cim.common.pojo;

/**
 * Function: 用户信息
 *
 * @author crossoverJie
 *         Date: 2018/12/24 02:33
 * @since JDK 1.8
 */
public class CIMUserInfo {
    private Long userId ;
    private String username ;

    public CIMUserInfo(Long userId, String username) {
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
        return "CIMUserInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
