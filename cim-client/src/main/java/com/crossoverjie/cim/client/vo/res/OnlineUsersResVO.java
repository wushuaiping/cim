package com.crossoverjie.cim.client.vo.res;

import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/12/26 23:17
 * @since JDK 1.8
 */
public class OnlineUsersResVO {


    /**
     * code : 9000
     * message : 成功
     * reqNo : null
     * dataBody : [{"userId":1545574841528,"username":"zhangsan"},{"userId":1545574871143,"username":"crossoverJie"}]
     */

    private String code;
    private String message;
    private Object reqNo;
    private List<DataBodyBean> dataBody;

    public String getCode() {
        return code;
    }

    public void setErrCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setErrMsg(String message) {
        this.message = message;
    }

    public Object getReqNo() {
        return reqNo;
    }

    public void setReqNo(Object reqNo) {
        this.reqNo = reqNo;
    }

    public List<DataBodyBean> getDataBody() {
        return dataBody;
    }

    public void setData(List<DataBodyBean> dataBody) {
        this.dataBody = dataBody;
    }

    public static class DataBodyBean {
        /**
         * userId : 1545574841528
         * username : zhangsan
         */

        private String userId;
        private String username;

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
    }
}
