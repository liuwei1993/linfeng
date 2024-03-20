package io.linfeng.api.app.user.service.impl;


import io.linfeng.api.app.user.request.UserUpdateRequest;
import io.linfeng.api.app.user.response.UserResponse;
import io.linfeng.api.app.user.service.AppUserService;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.Constant;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.common.utils.ObjectMapperUtil;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.service.config.service.DictItemService;
import io.linfeng.service.user.entity.UserEntity;
import io.linfeng.service.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {

    private final UserService userService;

    private final DictItemService dictItemService;

    public AppUserServiceImpl(UserService userService, DictItemService dictItemService) {
        this.userService = userService;
        this.dictItemService = dictItemService;
    }

    @Override
    public UserResponse getLoginUserInfo(Integer uid) {
        UserEntity user = userService.getUserByUid(uid);
        UserResponse userResponse = ObjectMapperUtil.convert(user, UserResponse.class);
        buildResponse(user, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse getUserResponseByUid(Integer uid) {
        UserEntity user = userService.getUserByUid(uid);
        UserResponse userResponse = ObjectMapperUtil.convert(user, UserResponse.class);
        buildResponse(user, userResponse);
        return userResponse;
    }

    @Override
    public void updateAppUserInfo(UserUpdateRequest userUpdateRequest, UserEntity appUser) {
        UserEntity user = userService.getById(appUser.getUid());

        if (!StringUtil.isEmpty(userUpdateRequest.getAvatar())) {
            user.setAvatar(userUpdateRequest.getAvatar());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getUserName())) {
            user.setUserName(userUpdateRequest.getUserName());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getStature())) {
            user.setStature(userUpdateRequest.getStature());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getWeight())) {
            user.setWeight(userUpdateRequest.getWeight());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getLivingCity())) {
            user.setLivingProvince(userUpdateRequest.getLivingProvince());
            user.setLivingCity(userUpdateRequest.getLivingCity());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getHomeCity())) {
            user.setHomeProvince(userUpdateRequest.getHomeProvince());
            user.setHomeCity(userUpdateRequest.getHomeCity());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getMarriage())) {
            user.setMarriage(userUpdateRequest.getMarriage());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getSchool())) {
            user.setSchool(userUpdateRequest.getSchool());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getEducation())) {
            user.setEducation(userUpdateRequest.getEducation());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getJob())) {
            user.setJob(userUpdateRequest.getJob());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getSalary())) {
            user.setSalary(userUpdateRequest.getSalary());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getIntro())) {
            user.setIntro(userUpdateRequest.getIntro());
        }
        userService.updateAndDeleteCache((user));
    }

    @Override
    public void updateAppUserInfoFirst(UserUpdateRequest userUpdateRequest, UserEntity user) {

        user.setUserName(userUpdateRequest.getUserName());

        user.setAvatar(userUpdateRequest.getAvatar());

        if (!StringUtil.isEmpty(userUpdateRequest.getGender())) {
            user.setGender(userUpdateRequest.getGender());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getLivingCity())) {
            user.setLivingProvince(userUpdateRequest.getLivingProvince());
            user.setLivingCity(userUpdateRequest.getLivingCity());
        }
        if (!StringUtil.isEmpty(userUpdateRequest.getBirthday())) {
            user.setBirthday(userUpdateRequest.getBirthday());
            try{
                user.setAge(DateUtil.getAge(userUpdateRequest.getBirthday()));
            }catch (Exception e){
                throw new LinfengException("获取年龄失败");
            }

        }

        userService.updateAndDeleteCache((user));

    }

    @Override
    public void updateUserAvatar(UserUpdateRequest userUpdateRequest, UserEntity user) {

        user.setAvatar(userUpdateRequest.getAvatar());
        userService.updateAndDeleteCache(user);
    }

    private void buildResponse(UserEntity user, UserResponse response){
        response.setMobile(StringUtil.maskMobile(user.getMobile()));
        if (!StringUtil.isEmpty(user.getGender())) {
            response.setGenderText(dictItemService.getItemName(Constant.DICT_GENDER, user.getGender()));
        }
        if (!StringUtil.isEmpty(user.getBirthday())) {
            response.setBirthdayYear(user.getBirthday().substring(2,4));
        }
        if (!StringUtil.isEmpty(user.getMarriage())) {
            response.setMarriageText(dictItemService.getItemName(Constant.DICT_MARRIAGE, user.getMarriage()));
        }
        if (!StringUtil.isEmpty(user.getEducation())) {
            response.setEducationText(dictItemService.getItemName(Constant.DICT_EDUCATION, user.getEducation()));
        }
        if (!StringUtil.isEmpty(user.getJob())) {
            response.setJobText(dictItemService.getItemName(Constant.DICT_JOB, user.getJob()));
        }
        if (!StringUtil.isEmpty(user.getSalary())) {
            response.setSalaryText(dictItemService.getItemName(Constant.DICT_SALARY, user.getSalary()));
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);

    }

}