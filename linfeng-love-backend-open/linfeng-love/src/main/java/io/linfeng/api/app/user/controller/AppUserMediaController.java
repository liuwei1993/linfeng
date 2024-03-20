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


import io.linfeng.api.app.user.request.UserMediaRequest;
import io.linfeng.api.app.user.response.UserMediaResponse;
import io.linfeng.api.app.user.service.AppUserMediaService;
import io.linfeng.common.annotation.Login;
import io.linfeng.common.annotation.LoginUser;
import io.linfeng.common.api.R;
import io.linfeng.common.api.Result;
import io.linfeng.common.utils.ValidatorUtils;
import io.linfeng.service.user.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 用户附件Api
 *
 */
@RestController
@RequestMapping("/app/media")
@Api(tags = "用户附件Api")
public class AppUserMediaController {


    private final AppUserMediaService appUserMediaService;

    public AppUserMediaController(AppUserMediaService appUserMediaService) {
        this.appUserMediaService = appUserMediaService;
    }

    /**
     * 用户附件列表
     * @param user 登录用户
     * @param request 查询请求
     * @return 用户附件列表
     */
    @Login
    @GetMapping("/list")
    @ApiOperation("用户附件列表")
    public Result<List<UserMediaResponse>> list(@ApiIgnore @LoginUser UserEntity user, UserMediaRequest request){
        List<UserMediaResponse> userMediaList = appUserMediaService.getUserMediaList(user.getUid(), request.getMediaType());
        return new Result<List<UserMediaResponse>>().ok(userMediaList);
    }

    /**
     * 用户附件上传
     * @param user 登录用户
     * @param requestList 附件列表
     * @return 上传结果
     */
    @Login
    @PostMapping("/upload")
    @ApiOperation("用户附件上传")
    public R upload(@ApiIgnore @LoginUser UserEntity user, @RequestBody List<UserMediaRequest> requestList){
        ValidatorUtils.validateEntity(requestList);
        appUserMediaService.uploadUserMedia(user, requestList);
        return R.ok("上传成功");
    }

    /**
     * 用户附件删除
     * @param user 登录用户
     * @param request 附件信息
     * @return 删除结果
     */
    @Login
    @PostMapping("/delete")
    @ApiOperation("删除用户附件")
    public R delete(@ApiIgnore @LoginUser UserEntity user, @RequestBody UserMediaRequest request){
        ValidatorUtils.validateEntity(request);
        appUserMediaService.deleteUserMedia(user, request);
        return R.ok("删除成功");
    }

}
