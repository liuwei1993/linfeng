package io.linfeng.api.app.guest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.linfeng.api.app.guest.request.GuestOperatorRequest;
import io.linfeng.api.app.guest.response.GuestResponse;
import io.linfeng.api.app.guest.service.AppGuestService;
import io.linfeng.api.app.moment.response.MomentMediaResponse;
import io.linfeng.api.app.moment.service.AppMomentService;
import io.linfeng.api.app.user.response.UserMediaResponse;
import io.linfeng.api.app.user.service.AppUserMediaService;
import io.linfeng.common.enums.CommonStatus;
import io.linfeng.common.enums.MediaType;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.Constant;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.common.utils.ObjectMapperUtil;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.service.config.service.DictItemService;
import io.linfeng.service.guest.entity.GuestRecommendEntity;
import io.linfeng.service.guest.enums.RecommendOperatorStatus;
import io.linfeng.service.guest.service.GuestRecommendService;
import io.linfeng.service.moment.entity.MomentEntity;
import io.linfeng.service.moment.service.MomentService;
import io.linfeng.service.user.entity.UserEntity;
import io.linfeng.service.user.entity.UserMediaEntity;
import io.linfeng.service.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("appGuestService")
public class AppGuestServiceImpl implements AppGuestService {

    private final GuestRecommendService guestRecommendService;

    private final AppUserMediaService appUserMediaService;

    private final UserService userService;

    private final MomentService momentService;

    private final AppMomentService appMomentService;

    private final DictItemService dictItemService;


    public AppGuestServiceImpl(GuestRecommendService guestRecommendService, AppUserMediaService appUserMediaService, UserService userService, MomentService momentService, AppMomentService appMomentService, DictItemService dictItemService) {
        this.guestRecommendService = guestRecommendService;
        this.appUserMediaService = appUserMediaService;
        this.userService = userService;
        this.momentService = momentService;
        this.appMomentService = appMomentService;
        this.dictItemService = dictItemService;
    }

    @Override
    @Transactional
    public GuestResponse newRecommend(UserEntity user) {

        LambdaQueryWrapper<GuestRecommendEntity> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(GuestRecommendEntity::getUid, user.getUid());
        wrapper.eq(GuestRecommendEntity::getStatus, RecommendOperatorStatus.DEFAULT.getValue());
        GuestRecommendEntity guestRecommendEntity = guestRecommendService.getOne(wrapper);
        if(guestRecommendEntity != null){
            UserEntity recommendGuest = userService.getUserByUid(guestRecommendEntity.getGuestUid());
            GuestResponse guestResponse = buildGuestResponse(recommendGuest);
            return guestResponse;
        }

        //新增一个推荐嘉宾
        //添加过滤条件：已经推荐过的嘉宾
        List<Integer> exitsGuestUidList = guestRecommendService.getExistGuestUidList(user.getUid());

        //添加过滤条件：默认配对反向性别
        Map<String, Object> params = new HashMap<>();
        if(user.getGender() == Constant.MAN){
            params.put("gender", Constant.WOMAN);
        }else{
            params.put("gender", Constant.MAN);
        }
        params.put("uidList", exitsGuestUidList);

        UserEntity recommendGuest = guestRecommendService.getRecommendGuest(params);

        if(recommendGuest == null){
            throw new LinfengException("找不到合适的嘉宾");
        }

        GuestRecommendEntity recommend = new GuestRecommendEntity();
        recommend.setUid(user.getUid());
        recommend.setGuestUid(recommendGuest.getUid());
        recommend.setStatus(RecommendOperatorStatus.DEFAULT.getValue());
        recommend.setCreateTime(DateUtil.nowDateTime());
        guestRecommendService.save(recommend);
        GuestResponse guestResponse = buildGuestResponse(recommendGuest);
        return guestResponse;

    }

    @Override
    public GuestResponse getGuestDetail(UserEntity user, Integer guestUid) {
        UserEntity guestUser = userService.getUserByUid(guestUid);
        GuestResponse guestResponse = buildGuestResponse(guestUser);
        return guestResponse;
    }

    @Override
    public List<GuestResponse> getHistoryRecommend(UserEntity user) {
        List<UserEntity> userList = guestRecommendService.getHistoryRecommend(user.getUid());
        List<GuestResponse> responseList = new ArrayList<>();
        userList.forEach(userEntity -> {
            GuestResponse response =  buildGuestResponse(userEntity);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public void recommendOperator(UserEntity user, GuestOperatorRequest request) {
        GuestRecommendEntity guestRecommendEntity = new GuestRecommendEntity();
        guestRecommendEntity.setStatus(RecommendOperatorStatus.DONE.getValue());
        guestRecommendEntity.setUpdateTime(DateUtil.nowDateTime());
        LambdaQueryWrapper<GuestRecommendEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GuestRecommendEntity::getUid, user.getUid());
        wrapper.eq(GuestRecommendEntity::getGuestUid, request.getUid());
        guestRecommendService.update(guestRecommendEntity, wrapper);
    }

    /**
     * 构建嘉宾返回对象
     * @param guestUser 嘉宾用户
     * @return 嘉宾信息返回对象
     */
    private GuestResponse buildGuestResponse(UserEntity guestUser){

        //用户基本信息
        GuestResponse response = transformerResponse(guestUser);

        //个人形象图片
        List<UserMediaResponse> userMediaList = appUserMediaService.getUserMediaList(guestUser.getUid(), MediaType.IMAGE.getValue());
        response.setUserMediaList(userMediaList);

        //个人动态数量
        LambdaQueryWrapper<MomentEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(MomentEntity::getUid, guestUser.getUid());
        lambdaQueryWrapper.eq(MomentEntity::getStatus, CommonStatus.CHECKED.getValue());
        response.setMomentListLength(momentService.count(lambdaQueryWrapper));

        //个人动态照片，最多展示四张
        List<MomentMediaResponse> momentMediaList = appMomentService.getMomentImageList(guestUser.getUid());
        response.setMomentMediaList(momentMediaList);

        return response;
    }

    /**
     * 转化对象
     * @param userEntity 用户
     * @return 用户响应
     */
    private GuestResponse transformerResponse(UserEntity userEntity){
        GuestResponse response = new GuestResponse();
        BeanUtils.copyProperties(userEntity, response);
        if (!StringUtil.isEmpty(userEntity.getGender())) {
            response.setGender(userEntity.getGender());
            response.setGenderText(dictItemService.getItemName(Constant.DICT_GENDER, userEntity.getGender()));
        }
        if (!StringUtil.isEmpty(userEntity.getBirthday())) {
            response.setBirthdayYear(userEntity.getBirthday().substring(2,4));
        }
        if (!StringUtil.isEmpty(userEntity.getMarriage())) {
            response.setMarriageText(dictItemService.getItemName(Constant.DICT_MARRIAGE, userEntity.getMarriage()));
        }
        if (!StringUtil.isEmpty(userEntity.getEducation())) {
            response.setEducationText(dictItemService.getItemName(Constant.DICT_EDUCATION, userEntity.getEducation()));
        }
        if (!StringUtil.isEmpty(userEntity.getJob())) {
            response.setJobText(dictItemService.getItemName(Constant.DICT_JOB, userEntity.getJob()));
        }
        if (!StringUtil.isEmpty(userEntity.getSalary())) {
            response.setSalaryText(dictItemService.getItemName(Constant.DICT_SALARY, userEntity.getSalary()));
        }
        return response;
    }

}