package com.crossoverjie.cim.route.controller;

import com.crossoverjie.cim.common.pojo.CIMUserInfo;
import com.crossoverjie.cim.common.pojo.RouteInfo;
import com.crossoverjie.cim.common.res.BaseResponse;
import com.crossoverjie.cim.common.res.NULLBody;
import com.crossoverjie.cim.common.route.algorithm.RouteHandle;
import com.crossoverjie.cim.common.util.RouteInfoParseUtil;
import com.crossoverjie.cim.route.api.RouteApi;
import com.crossoverjie.cim.route.api.vo.req.ChatReqVO;
import com.crossoverjie.cim.route.api.vo.req.LoginReqVO;
import com.crossoverjie.cim.route.api.vo.res.CIMServerResVO;
import com.crossoverjie.cim.route.api.vo.res.SimpleUserResponse;
import com.crossoverjie.cim.route.cache.ServerCache;
import com.crossoverjie.cim.route.service.AccountService;
import com.crossoverjie.cim.route.service.CommonBizService;
import com.crossoverjie.cim.route.service.UserInfoCacheService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 22/05/2018 14:46
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/cim/route")
public class RouteController implements RouteApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private ServerCache serverCache;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private CommonBizService commonBizService;

    @Autowired
    private RouteHandle routeHandle;

    @ApiOperation("客户端下线")
    @RequestMapping(value = "/offline", method = RequestMethod.POST)
    @Override
    public BaseResponse<NULLBody> offLine(@RequestBody ChatReqVO groupReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        CIMUserInfo cimUserInfo = userInfoCacheService.loadUserInfoByUserId(groupReqVO.getUserId());

        LOGGER.info("user [{}] offline!", cimUserInfo.toString());
        accountService.offLine(groupReqVO.getUserId());

        return res.success();
    }

    /**
     * 获取一台 CIM server
     *
     * @return
     */
    @ApiOperation("随机上线到一台服务器，并得到服务器信息")
    @RequestMapping(value = "/online", method = RequestMethod.POST)
    @Override
    public BaseResponse<CIMServerResVO> online(@RequestBody LoginReqVO loginReqVO) throws Exception {
        BaseResponse<CIMServerResVO> res = new BaseResponse<>();

        // 选择一台netty server
        String server = routeHandle.routeServer(serverCache.getServerList(), String.valueOf(loginReqVO.getUserId()));
        LOGGER.info("username=[{}] route server info=[{}]", loginReqVO.getUsername(), server);

        // 得到netty server解析成路由对象并检查netty server是否可用
        RouteInfo routeInfo = RouteInfoParseUtil.parse(server);
        commonBizService.checkServerAvailable(routeInfo);

        //登录到服务器
        accountService.loginServer(loginReqVO);

        //保存路由信息
        accountService.saveRouteInfo(loginReqVO, server);

        return res.success(new CIMServerResVO(routeInfo));
    }

    /**
     * 获取所选题组下到在线用户
     *
     * @param topicGroupId 题组id
     * @return
     */
    @GetMapping("/onlineUsers")
    public BaseResponse<List<SimpleUserResponse>> searchUsersByTopicGroup(@RequestParam("topicGroupId") String topicGroupId) {
        List<SimpleUserResponse> users = accountService.searchUsersByTopicGroup(topicGroupId);
        return new BaseResponse<List<SimpleUserResponse>>().success(users);
    }
}
