package io.linfeng.service.sms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.sms.dao.SmsCodeDao;
import io.linfeng.service.sms.entity.SmsCodeEntity;
import io.linfeng.service.sms.service.SmsCodeService;
import org.springframework.stereotype.Service;

import java.util.Map;



@Service("smsCodeService")
public class SmsCodeServiceImpl extends ServiceImpl<SmsCodeDao, SmsCodeEntity> implements SmsCodeService {

    @Override
    public PageObject queryPage(Map<String, Object> params) {
        long pageSize = Long.parseLong((String)params.get("limit"));
        long pageNum = Long.parseLong((String)params.get("page"));;
        IPage<SmsCodeEntity> page = this.page(
                new Page<>(pageNum, pageSize)
        );

        return new PageObject(page);
    }

}