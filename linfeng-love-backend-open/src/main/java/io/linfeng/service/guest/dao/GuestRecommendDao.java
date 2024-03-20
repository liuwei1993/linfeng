package io.linfeng.service.guest.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.guest.entity.GuestRecommendEntity;
import io.linfeng.service.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 推荐用户信息表
 * 
 * @author JiangCX
 * @date 2023-09-01 15:20:59
 */
@Mapper
public interface GuestRecommendDao extends BaseMapper<GuestRecommendEntity> {

    UserEntity getRecommendGuest(@Param("m") Map<String, Object> map);


    /**
     * 获取已经推荐过的嘉宾uid列表
     * @param uid 客户uid
     * @return 已经推荐过的嘉宾uid列表
     */
    List<Integer> getExistGuestUidList(@Param("uid")Integer uid);

    List<UserEntity> getHistoryRecommend(@Param("uid")Integer uid);


}
