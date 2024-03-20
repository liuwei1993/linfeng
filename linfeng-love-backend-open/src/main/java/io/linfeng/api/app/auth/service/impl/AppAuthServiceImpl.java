package io.linfeng.api.app.auth.service.impl;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.linfeng.api.app.auth.request.SmsLoginRequest;
import io.linfeng.api.app.auth.request.WxLoginRequest;
import io.linfeng.api.app.auth.response.LoginResponse;
import io.linfeng.api.app.auth.service.AppAuthService;
import io.linfeng.api.app.sms.service.AppSmsService;
import io.linfeng.common.enums.CommonStatus;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.common.utils.JwtUtil;
import io.linfeng.service.user.entity.UserEntity;
import io.linfeng.service.user.service.UserService;
import org.springframework.stereotype.Service;

@Service("appAuthService")
public class AppAuthServiceImpl implements AppAuthService {

    private final WxMaService wxMaService;

    private final UserService userService;

    private final AppSmsService appSmsService;

    private final JwtUtil jwtUtil;

    public AppAuthServiceImpl(WxMaService wxMaService, UserService userService, AppSmsService appSmsService, JwtUtil jwtUtil) {
        this.wxMaService = wxMaService;
        this.userService = userService;
        this.appSmsService = appSmsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse miniWxLogin(WxLoginRequest request) {
        LoginResponse response = new LoginResponse();
        String token = null;
        String sessionKey = null;
        String phone = null;
        try{
            sessionKey = wxMaService.getUserService().getSessionInfo(request.getCode()).getSessionKey();
            phone = wxMaService.getUserService().getPhoneNoInfo(sessionKey, request.getEncryptedData(), request.getIv()).getPhoneNumber();
        }catch (Exception e){
            throw new LinfengException("微信小程序登录获取手机号获取失败");
        }

        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserEntity::getMobile, phone);
        UserEntity userEntity = userService.getOne(lambdaQueryWrapper);
        if(userEntity == null){
            userEntity = new UserEntity();
            userEntity.setMobile(phone);
            userEntity.setCreateTime(DateUtil.nowDateTime());
            userService.save(userEntity);

        }

        token = jwtUtil.generateToken(userEntity.getUid());
        response.setToken(token);
        return response;
    }

    @Override
    public LoginResponse smsLogin(SmsLoginRequest request) {

        appSmsService.verifySmsCode(request.getMobileNo(), request.getCode());

        LoginResponse response = new LoginResponse();
        String token = null;

        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserEntity::getMobile, request.getMobileNo());
        UserEntity userEntity = userService.getOne(lambdaQueryWrapper);
        if(userEntity == null){
            userEntity = new UserEntity();
            userEntity.setMobile(request.getMobileNo());
            userEntity.setCreateTime(DateUtil.nowDateTime());
            userService.save(userEntity);
        }

        token = jwtUtil.generateToken(userEntity.getUid());
        response.setToken(token);
        return response;
    }

}