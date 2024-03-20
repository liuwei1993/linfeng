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
package io.linfeng.api.app.config.controller;

import io.linfeng.common.annotation.Login;
import io.linfeng.common.api.Result;
import io.linfeng.service.config.dto.request.DictRequest;
import io.linfeng.service.config.entity.DictItemEntity;
import io.linfeng.service.config.service.DictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 配置Api
 *
 * @author JiangCX
 * @date 2023-09-28 14:26:17
 */
@RestController
@RequestMapping("app/config")
@Api(tags = "配置Api")
public class AppConfigController {

    private final DictItemService dictItemService;

    public AppConfigController(DictItemService dictItemService) {
        this.dictItemService = dictItemService;
    }

    @Login
    @PostMapping("/dict/list")
    @ApiOperation("获取字典列表")
    public Result<List<DictItemEntity>> list(@RequestBody DictRequest dictRequest){
        return new Result<List<DictItemEntity>>().ok(dictItemService.getItemList(dictRequest.getTypeCode()));
    }

}
