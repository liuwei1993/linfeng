package io.linfeng.api.app.user.service;

import io.linfeng.api.app.user.response.UserResponse;
import io.linfeng.api.app.user.request.UserUpdateRequest;
import io.linfeng.service.user.entity.UserEntity;

/**
 * App用户服务
 *
 * @author JiangCX
 * @date 2023-09-06 16:22:14
 */
public interface AppUserService {

    /**
     * 获取登录用户信息
     * @param uid 用户id
     * @return 登录用户信息
     */
    UserResponse getLoginUserInfo(Integer uid);

    /**
     * 根据uid获取用户信息
     * @param uid 用户uid
     * @return 用户信息
     */
    UserResponse getUserResponseByUid(Integer uid);

    /**
     * 更新用户信息
     * @param userUpdateRequest 需要更新的信息
     * @param user 登录用户
     */
    void updateAppUserInfo(UserUpdateRequest userUpdateRequest, UserEntity user);

    /**
     * 首次补充个人资料
     * @param userUpdateRequest 需要更新的信息
     * @param user 登录用户
     */
    void updateAppUserInfoFirst(UserUpdateRequest userUpdateRequest, UserEntity user);

    /**
     * 修改头像
     * @param userUpdateRequest 需要更新的信息
     * @param user 登录用户
     */
    void updateUserAvatar(UserUpdateRequest userUpdateRequest, UserEntity user);

}

