package io.linfeng.service.sms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.sms.entity.SmsCodeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信验证码表
 * 
 * @author linfeng
 * @date 2023-11-20 09:08:31
 */
@Mapper
public interface SmsCodeDao extends BaseMapper<SmsCodeEntity> {
	
}
