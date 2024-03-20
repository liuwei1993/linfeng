package io.linfeng.service.user.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.user.entity.UserMediaEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户附件表
 * 
 * @author JiangCX
 * @date 2023-08-30 14:24:24
 */
@Mapper
public interface UserMediaDao extends BaseMapper<UserMediaEntity> {
	
}
