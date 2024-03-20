package io.linfeng.service.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.sms.entity.SmsCodeEntity;

import java.util.Map;

/**
 * 短信验证码表
 *
 * @author linfeng
 * @date 2023-11-20 09:08:31
 */
public interface SmsCodeService extends IService<SmsCodeEntity> {

    PageObject queryPage(Map<String, Object> params);
}

