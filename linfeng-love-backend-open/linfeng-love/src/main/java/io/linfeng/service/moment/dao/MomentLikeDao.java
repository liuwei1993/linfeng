package io.linfeng.service.moment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.moment.dto.response.MomentLikeResponseDTO;
import io.linfeng.service.moment.entity.MomentLikeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 动态点赞表
 * 
 * @author JiangCX
 * @date 2023-10-11 08:39:17
 */
@Mapper
public interface MomentLikeDao extends BaseMapper<MomentLikeEntity> {

}
