package io.linfeng.service.moment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.moment.dto.response.MomentMediaResponseDTO;
import io.linfeng.service.moment.entity.MomentMediaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动态附件表
 * 
 * @author JiangCX
 * @date 2023-10-12 09:53:39
 */
@Mapper
public interface MomentMediaDao extends BaseMapper<MomentMediaEntity> {

    List<MomentMediaResponseDTO> selectMomentImageList(@Param("uid")Integer uid);
}
