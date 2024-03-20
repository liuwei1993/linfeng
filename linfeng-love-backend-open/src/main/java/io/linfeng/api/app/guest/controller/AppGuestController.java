/**
 * -----------------------------------
 * Copyright (c) 2021-2024
 * All rights reserved, Designed By linfeng.tech , linfengtech.cn
 * 林风婚恋交友开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/yuncoder001/linfeng-love.git
 * 商业版授权联系技术客服	 QQ:  973921677/3582996245/793326982
 * 严禁分享、盗用、转卖源码或非法牟利！
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.api.app.guest.controller;


import io.linfeng.api.app.guest.request.GuestOperatorRequest;
import io.linfeng.api.app.guest.response.GuestResponse;
import io.linfeng.api.app.guest.service.AppGuestService;
import io.linfeng.common.annotation.Login;
import io.linfeng.common.annotation.LoginUser;
import io.linfeng.common.api.R;
import io.linfeng.common.api.Result;
import io.linfeng.service.user.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 嘉宾信息Api
 */
@RestController
@RequestMapping("/app/guest")
@Api(tags = "嘉宾信息Api")
public class AppGuestController {

    private final AppGuestService appGuestService;

    public AppGuestController(AppGuestService appGuestService) {
        this.appGuestService = appGuestService;
    }

    /**
     * 新增一个推荐嘉宾
     * @param user 登录用户
     */
    @Login
    @PostMapping ("/recommend/new")
    @ApiOperation("新增一个推荐嘉宾")
    public Result<GuestResponse> newRecommend(@ApiIgnore @LoginUser UserEntity user){
        GuestResponse response = appGuestService.newRecommend(user);
        return new Result<GuestResponse>().ok(response);
    }

    /**
     * 查询嘉宾主页信息
     * @param user 登录用户
     * @param guestUid 嘉宾uid
     * @return 嘉宾主页信息
     */
    @Login
    @GetMapping ("/detail")
    @ApiOperation("查询嘉宾主页信息")
    public Result<GuestResponse> detail(@ApiIgnore @LoginUser UserEntity user, Integer guestUid){
        GuestResponse latestRecommendGuest = appGuestService.getGuestDetail(user, guestUid);
        return new Result<GuestResponse>().ok(latestRecommendGuest);
    }

    /**
     * 查询历史推荐嘉宾列表
     * @param user 登录用户
     * @return 历史推荐嘉宾列表
     */
    @Login
    @GetMapping ("/recommend/history")
    @ApiOperation("历史推荐嘉宾列表")
    public Result<List<GuestResponse>> recommendHistory(@ApiIgnore @LoginUser UserEntity user){
        List<GuestResponse> historyRecommendList = appGuestService.getHistoryRecommend(user);
        return new Result<List<GuestResponse>>().ok(historyRecommendList);
    }

    /**
     * 推荐嘉宾操作
     * @param user 登录用户
     * @param request 操作请求
     */
    @Login
    @PostMapping("/recommend/operator")
    @ApiOperation("推荐嘉宾操作")
    public R recommendOperator(@ApiIgnore @LoginUser UserEntity user, @RequestBody GuestOperatorRequest request){
        appGuestService.recommendOperator(user, request);
        return R.ok();
    }

}
