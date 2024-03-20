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
package io.linfeng.api.app.moment.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.linfeng.api.app.moment.request.*;
import io.linfeng.api.app.moment.response.*;
import io.linfeng.service.user.entity.UserEntity;

import java.util.List;

/**
 * 动态服务
 *
 * @author JiangCX
 * @date 2023-09-28 14:26:17
 */
public interface AppMomentService {

    /**
     * 获取动态列表
     * @param user 用户
     * @param request 查询请求
     * @return 动态列表
     */
    IPage<MomentResponse> getMomentList(UserEntity user, MomentQueryRequest request);

    /**
     * 新增动态
     * @param user 用户
     * @param request 动态信息
     */
    void addMoment(UserEntity user, MomentRequest request);

    /**
     * 查询动态详情
     * @param user 用户
     * @param momentId 动态id
     * @return 动态详情
     */
    MomentResponse getMomentDetail(UserEntity user, Integer momentId);

    /**
     * 更新流量量
     * @param momentId 动态id
     */
    void updatePv(Integer momentId);

    /**
     * 更新点赞量
     * @param user 用户
     * @param request 更新请求
     */
    void updateLv(UserEntity user, MomentUpdateRequest request);

    /**
     * 获取评论列表
     * @param request 查询请求
     * @return 评论列表
     */
    IPage<MomentCommentResponse> getMomentCommentPage(CommentQueryRequest request);

    /**
     * 添加动态评论
     * @param user 登录用户
     * @param request 评论信息
     * @return 评论信息
     */
    MomentCommentResponse addMomentComment(UserEntity user, MomentCommentRequest request);

    /**
     * 删除评论
     * @param request 删除请求
     */
    void deleteMomentComment(MomentCommentRequest request);

    /**
     * 获取动态图片列表
     * @param momentId 动态id
     * @return 动态图片列表
     */
    List<MomentMediaResponse> getMomentImageList(Integer momentId);

    /**
     * 获取用户评论详情
     * @param commentId 评论id
     * @return 用户评论详情
     */
    CommentDetailResponse getMomentCommentDetail(Integer commentId);

    /**
     * 获取用户动态图片（最新四张）
     * @param uid 用户id
     * @return 用户动态图片
     */
    MomentImageResponse getUserMomentImage(Integer uid);

    /**
     * 删除评论
     * @param request 删除请求
     */
    void deleteMoment(MomentUpdateRequest request);

}

