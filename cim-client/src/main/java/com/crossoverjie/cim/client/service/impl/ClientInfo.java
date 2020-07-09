package com.crossoverjie.cim.client.service.impl;


import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2019-01-21 23:35
 * @since JDK 1.8
 */
@Component
public class ClientInfo {

    private Info info = new Info() ;

    public Info get(){
        return info ;
    }

    public ClientInfo saveUserInfo(String userId,String username){
        info.setUserId(userId);
        info.setUsername(username);
        return this;
    }


    public ClientInfo saveServiceInfo(String serviceInfo){
        info.setServiceInfo(serviceInfo);
        return this;
    }

    public ClientInfo saveStartDate(){
        info.setStartDate(new Date());
        return this;
    }

    public class Info{
        private String username;
        private String userId ;
        private String serviceInfo ;
        private Date startDate ;

        public Info() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getServiceInfo() {
            return serviceInfo;
        }

        public void setServiceInfo(String serviceInfo) {
            this.serviceInfo = serviceInfo;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }
    }
}
