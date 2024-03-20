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
package io.linfeng.api.app.moment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.linfeng.api.app.moment.request.*;
import io.linfeng.api.app.moment.response.*;
import io.linfeng.api.app.moment.service.AppMomentService;
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
 * 动态
 *
 * @author JiangCX
 * @date 2023-09-28 14:26:17
 */
@RestController
@RequestMapping("app/moment")
@Api(tags = "动态Api")
public class AppMomentController {

    private final AppMomentService appMomentService;

    public AppMomentController(AppMomentService appMomentService) {
        this.appMomentService = appMomentService;
    }

    /**
     * 查询动态列表
     * @param user 登录用户
     * @param request 查询请求
     * @return 动态列表
     */
    @Login
    @GetMapping("/list")
    @ApiOperation("动态列表")
    public Result<IPage<MomentResponse>> list(@ApiIgnore @LoginUser UserEntity user, MomentQueryRequest request){
        IPage<MomentResponse> momentList = appMomentService.getMomentList(user, request);
        return new Result<IPage<MomentResponse>>().ok(momentList);
    }

    /**
     * 动态附件信息（最新的4张图片）
     * @param user 登录用户
     * @return 动态附件列表
     */
    @Login
    @GetMapping("/media/list")
    @ApiOperation("动态附件信息")
    public Result<MomentImageResponse> mediaList(@ApiIgnore @LoginUser UserEntity user){
        MomentImageResponse momentImage = appMomentService.getUserMomentImage(user.getUid());
        return new Result<MomentImageResponse>().ok(momentImage);
    }

    /**
     * 动态发布
     * @param user 登录用户
     * @param request 发布请求
     * @return 发布结果
     */
    @Login
    @ApiOperation(value = "发布动态")
    @PostMapping("/add")
    public R add(@ApiIgnore @LoginUser UserEntity user, @RequestBody MomentRequest request) {
        appMomentService.addMoment(user, request);
        return R.ok();
    }

    /**
     * 查询动态详情
     * @param user 登录用户
     * @param request 查询请求
     * @return 动态详情
     */
    @Login
    @GetMapping("/detail")
    @ApiOperation("动态详情")
    public Result<MomentResponse> list(@ApiIgnore @LoginUser UserEntity user, MomentRequest request){
        MomentResponse moment = appMomentService.getMomentDetail(user, request.getMomentId());
        return new Result<MomentResponse>().ok(moment);
    }

    /**
     * 删除动态
     * @param request 删除请求
     * @return 删除结果
     */
    @Login
    @ApiOperation(value = "删除动态")
    @PostMapping("/delete")
    public R deleteMoment(@RequestBody MomentUpdateRequest request) {
        appMomentService.deleteMoment(request);
        return R.ok();
    }

    /**
     * 更新浏览量
     * @param request 更新请求
     * @return 更新结果
     */
    @Login
    @ApiOperation(value = "更新浏览量")
    @PostMapping("/updatePv")
    public R updatePv(@RequestBody MomentUpdateRequest request) {
        appMomentService.updatePv(request.getMomentId());
        return R.ok();
    }

    /**
     * 更新点赞状态
     * @param user 登录用户
     * @param request 更新请求
     * @return 更新结果
     */
    @Login
    @ApiOperation(value = "更新点赞状态")
    @PostMapping("/updateLv")
    public R like(@ApiIgnore @LoginUser UserEntity user, @RequestBody MomentUpdateRequest request) {
        appMomentService.updateLv(user, request);
        return R.ok();
    }

    /**
     * 查询评论列表
     * @param request 查询请求
     * @return 评论列表
     */
    @Login
    @GetMapping("/comment/list")
    @ApiOperation("评论列表")
    public Result<IPage<MomentCommentResponse>> list(CommentQueryRequest request){
        IPage<MomentCommentResponse> commentList = appMomentService.getMomentCommentPage(request);
        return new Result<IPage<MomentCommentResponse>>().ok(commentList);
    }

    /**
     * 查询评论详情
     * @param commentId 评论id
     * @return 评论详情
     */
    @Login
    @GetMapping("/comment/detail")
    @ApiOperation("评论详情")
    public Result<CommentDetailResponse> detail(Integer commentId){
        CommentDetailResponse comment = appMomentService.getMomentCommentDetail(commentId);
        return new Result<CommentDetailResponse>().ok(comment);
    }

    /**
     * 发表评论
     * @param user 登录用户
     * @param request 评论请求
     * @return 发表结果
     */
    @Login
    @ApiOperation(value = "发表评论")
    @PostMapping("/comment/add")
    public Result<MomentCommentResponse> add(@ApiIgnore @LoginUser UserEntity user, @RequestBody MomentCommentRequest request) {
        MomentCommentResponse response = appMomentService.addMomentComment(user, request);
        return new Result<MomentCommentResponse>().ok(response);
    }

    /**
     * 删除评论
     * @param request 删除请求
     * @return 删除结果
     */
    @Login
    @ApiOperation(value = "删除评论")
    @PostMapping("/comment/delete")
    public R deleteMomentComment(@RequestBody MomentCommentRequest request) {
        appMomentService.deleteMomentComment(request);
        return R.ok();
    }

}
