package com.crossoverjie.cim.route.api.vo.req;

import com.crossoverjie.cim.common.req.BaseRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/12/23 22:04
 * @since JDK 1.8
 */
public class RegisterInfoReqVO extends BaseRequest {

    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(required = true, value = "username", example = "zhangsan")
    private String username ;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RegisterInfoReqVO{" +
                "username='" + username + '\'' +
                "} " + super.toString();
    }
}
