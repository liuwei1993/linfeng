package io.linfeng.api.app.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.linfeng.api.app.sms.service.AppSmsService;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.service.sms.entity.SmsCodeEntity;
import io.linfeng.service.sms.service.SmsCodeService;
import org.springframework.stereotype.Service;


@Service("appSmsService")
public class AppSmsServiceImpl implements AppSmsService {

    private static final Integer MAX_UNVERIFIED_COUNT = 5;

    private static final Integer MAX_FAILED_COUNT = 5;

    private static final Integer SEND_INTERVAL_SECONDS = 60;

    private static final Integer EXPIRED_SECONDS = 5 * 60;

    private static final String[] STR = {"0","1","2","3","4","5","6","7","8","9"};

    private static final Integer STATUS_WAITING = 0;

    private static final Integer STATUS_VERIFIED = 1;

    private final SmsCodeService smsCodeService;

    public AppSmsServiceImpl(SmsCodeService smsCodeService) {
        this.smsCodeService = smsCodeService;
    }

    @Override
    public String sendSmsCode(String mobileNo) {
        LambdaQueryWrapper<SmsCodeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsCodeEntity::getMobileNo, mobileNo);
        SmsCodeEntity smsCode = smsCodeService.getOne(wrapper);
        if(smsCode == null || cleanExpired(smsCode)){
            smsCode = new SmsCodeEntity();
            String code = generateSmsCode();
            smsCode.setSmsCode(code);
            smsCode.setSendTime(DateUtil.nowDateTime());
            smsCode.setMobileNo(mobileNo);
            smsCode.setSendCount(1);
            smsCode.setFailedCount(0);
            smsCode.setStatus(STATUS_WAITING);
            smsCode.setCreateTime(DateUtil.nowDateTime());
            smsCodeService.save(smsCode);
            return code;
        }

        if(DateUtil.secondsBetween(smsCode.getSendTime(), DateUtil.nowDateTime()) < SEND_INTERVAL_SECONDS){
            throw new LinfengException("验证码发送频繁");
        }

        if(smsCode.getSendCount() >= MAX_UNVERIFIED_COUNT){
            throw new LinfengException("验证码发送达到上限");
        }

        String code = generateSmsCode();
        smsCode.setSendTime(DateUtil.nowDateTime());
        smsCode.setSmsCode(code);
        smsCode.setSendCount(smsCode.getSendCount() + 1);
        smsCode.setStatus(STATUS_WAITING);
        smsCode.setUpdateTime(DateUtil.nowDateTime());
        smsCodeService.updateById(smsCode);
        return code;

    }

    @Override
    public void verifySmsCode(String mobileNo, String code) {
        LambdaQueryWrapper<SmsCodeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SmsCodeEntity::getMobileNo, mobileNo);
        SmsCodeEntity smsCode = smsCodeService.getOne(wrapper);

        if(smsCode == null){
            throw new LinfengException("验证码错误");
        }

        if(smsCode.getFailedCount() >= MAX_FAILED_COUNT){
            throw new LinfengException("错误次数达到上限");
        }

        smsCode.setSendCount(1);

        if(!code.equals(smsCode.getSmsCode())){
            smsCode.setFailedCount(smsCode.getFailedCount() + 1);
            smsCode.setStatus(STATUS_VERIFIED);
            smsCodeService.updateById(smsCode);
            throw new LinfengException("验证码错误");
        }

        if(STATUS_VERIFIED == smsCode.getStatus()){
            throw new LinfengException("验证码已失效");
        }

        if(DateUtil.secondsBetween(smsCode.getSendTime(), DateUtil.nowDateTime()) > EXPIRED_SECONDS){
            throw new LinfengException("验证码已失效");
        }

        smsCode.setStatus(STATUS_VERIFIED);
        smsCodeService.updateById(smsCode);

    }

    /**
     * 重置发送时间
     * @param smsCode 短信验证码
     * @return 重置结果
     */
    private boolean cleanExpired(SmsCodeEntity smsCode){
        if(DateUtil.daysBetween(smsCode.getSendTime(), DateUtil.nowDateTime()) >= 1){
            smsCodeService.removeById(smsCode.getId());
            return true;
        }
        return false;
    }

    private String generateSmsCode() {
        StringBuffer smsCode = new StringBuffer();
        for(int i=0; i<6; i++){
            int index = (int)(Math.random() * 10);
            smsCode.append(STR[index]);
        }
        return smsCode.toString();
    }
}