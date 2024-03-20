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
package io.linfeng.api.app.sms.service;


/**
 * 短信验证码业务服务
 *
 * @author JiangCX
 * @date 2023-09-28 14:26:17
 */
public interface AppSmsService {

    /**
     * 发送短信验证码
     * @param mobileNo 手机号
     */
    String sendSmsCode(String mobileNo);

    /**
     * 验证短信验证码
     * @param mobileNo 手机号
     * @param code 验证码
     */
    void verifySmsCode(String mobileNo, String code);

}

