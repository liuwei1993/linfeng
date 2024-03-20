package io.linfeng.api.app.user.service.impl;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.linfeng.api.app.user.response.UserMediaResponse;
import io.linfeng.api.app.user.request.UserMediaRequest;
import io.linfeng.api.app.user.service.AppUserMediaService;
import io.linfeng.common.utils.ObjectMapperUtil;
import io.linfeng.service.user.entity.UserEntity;
import io.linfeng.service.user.entity.UserMediaEntity;
import io.linfeng.service.user.service.UserMediaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("appUserMediaService")
public class AppUserMediaServiceImpl implements AppUserMediaService {

    private final UserMediaService userMediaService;

    public AppUserMediaServiceImpl(UserMediaService userMediaService) {
        this.userMediaService = userMediaService;
    }

    @Override
    public List<UserMediaResponse> getUserMediaList(Integer uid, Integer mediaType) {
        LambdaQueryWrapper<UserMediaEntity> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(UserMediaEntity::getUid, uid);
        wrapper.eq(UserMediaEntity::getMediaType, mediaType);
        List<UserMediaEntity> userMediaList = userMediaService.list(wrapper);
        return ObjectMapperUtil.convert(userMediaList, UserMediaResponse.class);
    }

    @Override
    public void uploadUserMedia(UserEntity user, List<UserMediaRequest> requestList) {
        List<UserMediaEntity> userMediaList = new ArrayList<>();

        requestList.forEach(request -> {
            UserMediaEntity userMediaEntity  = new UserMediaEntity();
            userMediaEntity.setUid(user.getUid());
            userMediaEntity.setMediaName(IdUtil.fastSimpleUUID());
            userMediaEntity.setMediaType(request.getMediaType());
            userMediaEntity.setUrl(request.getUrl());
            userMediaList.add(userMediaEntity);
        });

        userMediaService.saveBatch(userMediaList);

    }

    @Override
    public void deleteUserMedia(UserEntity user, UserMediaRequest request) {
        LambdaQueryWrapper<UserMediaEntity> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(UserMediaEntity::getUid,user.getUid());
        wrapper.eq(UserMediaEntity::getMediaName,request.getMediaName());
        wrapper.eq(UserMediaEntity::getMediaType,request.getMediaType());
        userMediaService.remove(wrapper);
    }
}