package com.crossoverjie.cim.route.api;

import com.crossoverjie.cim.route.api.vo.req.OfflineRequest;
import com.crossoverjie.cim.route.api.vo.req.OnlineRequest;

/**
 * Function: Route Api
 *
 * @author crossoverJie
 * Date: 2020-04-24 23:43
 * @since JDK 1.8
 */
public interface RouteApi {

    /**
     * Offline account
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object offLine(OfflineRequest groupReqVO) throws Exception;

    /**
     * Login account
     *
     * @param loginReqVO
     * @return
     * @throws Exception
     */
    Object online(OnlineRequest loginReqVO) throws Exception;
}
