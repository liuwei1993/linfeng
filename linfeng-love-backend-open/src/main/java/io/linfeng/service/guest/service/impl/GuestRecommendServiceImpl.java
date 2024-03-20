package io.linfeng.service.guest.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.service.guest.dao.GuestRecommendDao;
import io.linfeng.service.guest.entity.GuestRecommendEntity;
import io.linfeng.service.guest.service.GuestRecommendService;
import io.linfeng.service.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("guestRecommendService")
public class GuestRecommendServiceImpl extends ServiceImpl<GuestRecommendDao, GuestRecommendEntity> implements GuestRecommendService {

    @Override
    public UserEntity getRecommendGuest(Map<String, Object> params) {
        return this.baseMapper.getRecommendGuest(params);
    }

    @Override
    public List<Integer> getExistGuestUidList(Integer uid) {
        return this.baseMapper.getExistGuestUidList(uid);
    }

    @Override
    public List<UserEntity> getHistoryRecommend(Integer uid) {
        return this.baseMapper.getHistoryRecommend(uid);
    }


}