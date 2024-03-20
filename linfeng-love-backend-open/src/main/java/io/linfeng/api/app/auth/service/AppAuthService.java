package io.linfeng.api.app.auth.service;

import io.linfeng.api.app.auth.request.SmsLoginRequest;
import io.linfeng.api.app.auth.request.WxLoginRequest;
import io.linfeng.api.app.auth.response.LoginResponse;

/**
 * App登录授权服务
 *
 * @author JiangCX
 * @date 2023-09-06 16:22:14
 */
public interface AppAuthService {

    /**
     * 微信小程序静默登录
     * @param request 请求信息
     * @return 登录结果
     */
    LoginResponse miniWxLogin(WxLoginRequest request);

    /**
     * 短信验证码登录
     * @param request 请求信息
     * @return 登录结果
     */
    LoginResponse smsLogin(SmsLoginRequest request);


}

