package com.crossoverjie.cim.route.controller;

import com.crossoverjie.cim.common.pojo.CIMUserInfo;
import com.crossoverjie.cim.common.pojo.RouteInfo;
import com.crossoverjie.cim.common.res.BaseResponse;
import com.crossoverjie.cim.common.res.NULLBody;
import com.crossoverjie.cim.route.api.RouteApi;
import com.crossoverjie.cim.route.api.vo.req.OfflineRequest;
import com.crossoverjie.cim.route.api.vo.req.OnlineRequest;
import com.crossoverjie.cim.route.api.vo.res.CIMServerResVO;
import com.crossoverjie.cim.route.service.AccountService;
import com.crossoverjie.cim.route.service.UserInfoCacheService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 路由服务
 *
 * @author crossoverJie
 * Date: 22/05/2018 14:46
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/cim/route")
@AllArgsConstructor
public class RouteController implements RouteApi {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    private final AccountService accountService;

    private final UserInfoCacheService userInfoCacheService;

    /**
     * 客户端下线
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @ApiOperation("客户端下线")
    @RequestMapping(value = "/offline", method = RequestMethod.POST)
    @Override
    public BaseResponse<NULLBody> offLine(@RequestBody OfflineRequest offlineRequest) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse<>();

        CIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(offlineRequest.getUserId());

        LOGGER.info("user [{}] offline!", cimUserInfo.toString());
        accountService.offline(offlineRequest);

        return res.success();
    }

    /**
     * 客户端上线，并得到服务器信息
     *
     * @return
     */
    @ApiOperation("随机上线到一台服务器，并得到服务器信息")
    @RequestMapping(value = "/online", method = RequestMethod.POST)
    @Override
    public BaseResponse<CIMServerResVO> online(@RequestBody OnlineRequest loginReqVO) throws Exception {

        BaseResponse<CIMServerResVO> res = new BaseResponse<>();

        RouteInfo routeInfo = accountService.online(loginReqVO);

        return res.success(new CIMServerResVO(routeInfo));
    }

    /**
     * 获取所选题组下到在线用户
     *
     * @param topicGroupId 题组id
     * @return
     */
    @GetMapping("/onlineUsers")
    public BaseResponse<Set<CIMUserInfo>> searchUsersByTopicGroup(@RequestParam("topicGroupId") String topicGroupId) {
        Set<CIMUserInfo> users = accountService.searchUsersByTopicGroup(topicGroupId);
        return new BaseResponse<Set<CIMUserInfo>>().success(users);
    }
}
