package io.linfeng.service.guest.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.service.guest.entity.GuestRecommendEntity;
import io.linfeng.service.user.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 推荐用户信息表
 *
 * @author JiangCX
 * @date 2023-09-01 15:20:59
 */
public interface GuestRecommendService extends IService<GuestRecommendEntity> {

    UserEntity getRecommendGuest(Map<String, Object> params);


    /**
     * 获取已经推荐过的嘉宾uid列表
     * @param uid 客户uid
     * @return 已经推荐过的嘉宾uid列表
     */
    List<Integer> getExistGuestUidList(Integer uid);

    List<UserEntity> getHistoryRecommend(Integer uid);
}

