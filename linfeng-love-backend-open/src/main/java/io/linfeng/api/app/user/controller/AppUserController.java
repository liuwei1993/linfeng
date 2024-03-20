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
package io.linfeng.api.app.user.controller;


import io.linfeng.api.app.user.request.UserUpdateRequest;
import io.linfeng.api.app.user.response.UserResponse;
import io.linfeng.api.app.user.service.AppUserService;
import io.linfeng.common.annotation.Login;
import io.linfeng.common.annotation.LoginUser;
import io.linfeng.common.api.R;
import io.linfeng.common.api.Result;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.common.utils.ValidatorUtils;
import io.linfeng.service.user.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 用户Api
 *
 */
@RestController
@RequestMapping("/app/user")
@Api(tags = "用户Api")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


    /**
     * 获取登录用户信息
     * @param user 登录用户
     * @return 登录用户
     */
    @Login
    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public Result<UserResponse> userInfo(@ApiIgnore @LoginUser UserEntity user){
        UserResponse response= appUserService.getLoginUserInfo(user.getUid());
        return new Result<UserResponse>().ok(response);
    }

    /**
     * 获取用户信息（根据uid）
     * @param uid 用户uid
     * @return 用户信息
     */
    @Login
    @GetMapping("/getByUid")
    @ApiOperation("获取用户信息（根据uid）")
    public Result<UserResponse> userInfo(Integer uid){
        UserResponse response= appUserService.getUserResponseByUid(uid);
        return new Result<UserResponse>().ok(response);
    }

    /**
     * 修改用户信息
     * @param user 登录用户
     * @param userUpdateRequest 需要更新的信息
     * @return 修改结果
     */
    @Login
    @PostMapping("/userInfoEdit")
    @ApiOperation("用户修改个人信息")
    public R userInfoEdit(@ApiIgnore @LoginUser UserEntity user, @RequestBody UserUpdateRequest userUpdateRequest){
        ValidatorUtils.validateEntity(userUpdateRequest);
        appUserService.updateAppUserInfo(userUpdateRequest,user);
        return R.ok("修改成功");
    }

    /**
     * 新用户首次补充个人信息
     * @param user 登录用户
     * @param userUpdateRequest 需要更新的信息
     * @return 修改结果
     */
    @Login
    @PostMapping("/userInfoFirstEdit")
    @ApiOperation("用户首次填写个人信息")
    public R userInfoFirstEdit(@ApiIgnore @LoginUser UserEntity user, @RequestBody UserUpdateRequest userUpdateRequest){
        ValidatorUtils.validateEntity(userUpdateRequest);
        appUserService.updateAppUserInfoFirst(userUpdateRequest,user);
        return R.ok("设置成功");
    }

    /**
     * 修改头像
     * @param user 登录用户
     * @param userUpdateRequest 更新请求
     * @return 修改结果
     */
    @Login
    @PostMapping("/avatarEdit")
    @ApiOperation("修改头像")
    public R avatarEdit(@ApiIgnore @LoginUser UserEntity user, @RequestBody UserUpdateRequest userUpdateRequest){
        ValidatorUtils.validateEntity(userUpdateRequest);
        appUserService.updateUserAvatar(userUpdateRequest,user);
        return R.ok("设置成功");
    }

}
