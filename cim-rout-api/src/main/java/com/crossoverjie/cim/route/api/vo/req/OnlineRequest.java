package com.crossoverjie.cim.route.api.vo.req;

import com.crossoverjie.cim.common.req.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2018/12/23 22:30
 * @since JDK 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OnlineRequest extends BaseRequest {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 当前用户到等级
     */
    private String level;

    /**
     * 需要进行比赛的题组id
     */
    private String topicGroupId;

    /**
     * 用户名
     */
    private String username;

}
