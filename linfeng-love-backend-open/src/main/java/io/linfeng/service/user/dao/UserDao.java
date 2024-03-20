package io.linfeng.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author JiangCX
 * @email 2445465217@qq.com
 * @date 2023-01-20 12:10:43
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
