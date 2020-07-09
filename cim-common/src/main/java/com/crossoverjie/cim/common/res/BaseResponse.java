package com.crossoverjie.cim.common.res;

import com.crossoverjie.cim.common.enums.StatusEnum;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {

    /**
     * 相应成功失败，默认成功
     */
    private boolean success = true;

    /**
     * 返回内容
     */
    private T data;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误信息
     */
    private String errMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public BaseResponse<T> success(T data) {
        this.data = data;
        this.success = true;
        this.errCode = StatusEnum.SUCCESS.code();
        this.errMsg = StatusEnum.SUCCESS.message();
        return this;
    }

    public BaseResponse<T> success() {
        this.success = true;
        this.errCode = StatusEnum.SUCCESS.code();
        this.errMsg = StatusEnum.SUCCESS.message();
        return this;
    }

    public BaseResponse<T> error(String code, String message) {
        this.success = false;
        this.errCode= code;
        this.errMsg = message;
        return this;
    }

    public BaseResponse<T> error(StatusEnum statusEnum) {
        this.success = false;
        this.errCode = statusEnum.code();
        this.errMsg = statusEnum.message();
        return this;
    }
}