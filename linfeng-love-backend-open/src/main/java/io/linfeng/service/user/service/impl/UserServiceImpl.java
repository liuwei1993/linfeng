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
package io.linfeng.service.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.api.PageObject;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.common.utils.RedisUtil;
import io.linfeng.common.utils.StringUtil;
import io.linfeng.service.user.dao.UserDao;
import io.linfeng.service.user.entity.UserEntity;
import io.linfeng.service.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    private static final String CACHE_USER_UID_PREFIX = "uid:";
    private final RedisUtil redisUtils;

    public UserServiceImpl(RedisUtil redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public PageObject queryPage(Map<String, Object> params) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        //模糊查询
        String key = (String) params.get("key");

        if (!StringUtil.isEmpty(key)) {
            if (NumberUtil.isInteger(key)) {
                queryWrapper.lambda().eq(UserEntity::getUid, key);
            }
            queryWrapper.lambda().like(UserEntity::getUserName, key)
                    .or().like(UserEntity::getMobile, key);
        }

        queryWrapper.lambda().orderByDesc(UserEntity::getUid);
        long pageSize = Long.parseLong((String)params.get("limit"));
        long pageNum = Long.parseLong((String)params.get("page"));;
        IPage<UserEntity> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        //如果不需要对手机号打码就注释掉下面这段
        List<UserEntity> records = page.getRecords();
        records.forEach(user -> {
            if (!StringUtil.isEmpty(user.getMobile())) {
                user.setMobile(StringUtil.maskMobile(user.getMobile()));
            }
        });
        page.setRecords(records);


        return new PageObject(page);
    }

    @Override
    public void updateAndDeleteCache(@NotNull UserEntity user) {
        user.setUpdateTime(DateUtil.nowDateTime());
        this.updateById(user);
        redisUtils.delete(CACHE_USER_UID_PREFIX + user.getUid());
    }

    @Override
    public UserEntity getUserByUid(int uid) {
        UserEntity userInfo = redisUtils.get(CACHE_USER_UID_PREFIX + uid, UserEntity.class);
        if (userInfo != null) {
            return userInfo;
        }
        //重新获取用户信息
        UserEntity user = this.getById(uid);
        redisUtils.set(CACHE_USER_UID_PREFIX + uid, user, 7200);
        return user;
    }

}