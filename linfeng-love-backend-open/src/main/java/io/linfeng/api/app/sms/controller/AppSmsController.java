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
package io.linfeng.api.app.sms.controller;

import io.linfeng.api.app.sms.request.SmsCodeRequest;
import io.linfeng.api.app.sms.service.AppSmsService;
import io.linfeng.common.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;



/**
 * 短信Api
 *
 * @author JiangCX
 * @date 2023-09-28 14:26:17
 */
@RestController
@RequestMapping("app/sms")
@Api(tags = "短信Api")
public class AppSmsController {

    private final AppSmsService appSmsService;

    public AppSmsController(AppSmsService appSmsService) {
        this.appSmsService = appSmsService;
    }

    /**
     * 发送短信验证码
     * @param request 发送请求
     * @return 短信验证码
     */
    @PostMapping("/send")
    @ApiOperation("发送短信验证码")
    public R send(@RequestBody SmsCodeRequest request){
        //生产环境需要改掉，不能把验证码返回给前端
        String code = appSmsService.sendSmsCode(request.getMobileNo());
        return R.ok(code);
    }

}
