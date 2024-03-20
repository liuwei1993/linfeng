package io.linfeng.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.user.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author JiangCX
 * @email 2445465217@qq.com
 * @date 2023-08-22 12:10:43
 */
public interface UserService extends IService<UserEntity> {

    PageObject queryPage(Map<String, Object> params);



    /**
     * 更新并删除缓存
     * @param user 用户信息
     */
    void updateAndDeleteCache(UserEntity user);

    /**
     * 根据uid获取用户信息
     * @param uid 用户uid
     * @return 用户信息
     */
    UserEntity getUserByUid(int uid);

}

