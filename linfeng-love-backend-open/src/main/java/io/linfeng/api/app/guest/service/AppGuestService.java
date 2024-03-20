/**
 * -----------------------------------
 * Copyright (c) 2021-2024
 * All rights reserved, Designed By linfeng.tech , linfengtech.cn
 * 林风婚恋交友开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 * 商业版授权联系技术客服	 QQ:  973921677/3582996245
 * 严禁分享、盗用、转卖源码或非法牟利！
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.api.app.guest.service;


import io.linfeng.api.app.guest.request.GuestOperatorRequest;
import io.linfeng.api.app.guest.response.GuestResponse;
import io.linfeng.service.user.entity.UserEntity;

import java.util.List;

/**
 * 嘉宾业务服务
 *
 * @author JiangCX
 * @date 2023-09-28 14:26:17
 */
public interface AppGuestService {

    /**
     * 新增一个推荐
     * @param user 登录用户
     */
    GuestResponse newRecommend(UserEntity user);

    /**
     * 获取嘉宾主页信息
     * @param user 登录用户
     * @param guestUid 嘉宾uid
     * @return 嘉宾主页信息
     */
    GuestResponse getGuestDetail(UserEntity user, Integer guestUid);


    List<GuestResponse> getHistoryRecommend(UserEntity user);

    void recommendOperator(UserEntity user, GuestOperatorRequest request);
}

