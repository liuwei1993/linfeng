package io.linfeng.api.app.user.service;

import io.linfeng.api.app.user.response.UserMediaResponse;
import io.linfeng.api.app.user.request.UserMediaRequest;
import io.linfeng.service.user.entity.UserEntity;

import java.util.List;

/**
 * 用户附件信息业务服务
 *
 * @author JiangCX
 * @date 2023-09-06 16:22:14
 */
public interface AppUserMediaService {

    /**
     * 用户附件列表
     * @param uid 登录用户
     * @param mediaType 查询条件
     * @return 用户附件列表
     */
    List<UserMediaResponse> getUserMediaList(Integer uid, Integer mediaType);

    /**
     * 用户附件上传
     * @param user 登录用户
     * @param requestList 附件列表
     * @return 上传结果
     */
    void uploadUserMedia(UserEntity user, List<UserMediaRequest> requestList);

    /**
     * 用户附件删除
     * @param user 登录用户
     * @param request 附件信息
     * @return 删除结果
     */
    void deleteUserMedia(UserEntity user, UserMediaRequest request);
}

