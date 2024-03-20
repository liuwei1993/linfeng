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
package io.linfeng.api.app.auth.controller;


import io.linfeng.api.app.auth.request.SmsLoginRequest;
import io.linfeng.api.app.auth.request.WxLoginRequest;
import io.linfeng.api.app.auth.response.LoginResponse;
import io.linfeng.api.app.auth.service.AppAuthService;
import io.linfeng.common.api.Result;
import io.linfeng.common.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录授权Api
 */
@RestController
@RequestMapping("/app/auth")
@Api(tags = "用户登录授权Api")
public class AppAuthController {

    private final AppAuthService appAuthService;

    public AppAuthController(AppAuthService appAuthService) {
        this.appAuthService = appAuthService;
    }

    /**
     * 微信小程序登录
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/miniWxLogin")
    @ApiOperation("微信小程序登录")
    public Result<LoginResponse> miniWxLogin(@RequestBody WxLoginRequest request){

        ValidatorUtils.validateEntity(request);
        LoginResponse loginResponse = appAuthService.miniWxLogin(request);

        return new Result<LoginResponse>().ok(loginResponse);
    }

    /**
     * 手机验证码登录
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/smsLogin")
    @ApiOperation("手机验证码登录")
    public Result<LoginResponse> smsLogin(@RequestBody SmsLoginRequest request){

        LoginResponse loginResponse = appAuthService.smsLogin(request);

        return new Result<LoginResponse>().ok(loginResponse);
    }

}
