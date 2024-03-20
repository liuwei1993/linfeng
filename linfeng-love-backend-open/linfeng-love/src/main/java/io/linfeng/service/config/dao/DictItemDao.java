package io.linfeng.service.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.config.entity.DictItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典子项表
 * 
 * @author JiangCX
 * @date 2023-11-13 10:09:12
 */
@Mapper
public interface DictItemDao extends BaseMapper<DictItemEntity> {
	
}
