package io.linfeng.service.moment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.service.moment.dto.response.MomentCommentResponseDTO;
import io.linfeng.service.moment.entity.MomentCommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动态评论表
 * 
 * @author JiangCX
 * @date 2023-10-10 10:46:11
 */
@Mapper
public interface MomentCommentDao extends BaseMapper<MomentCommentEntity> {

    List<MomentCommentResponseDTO> selectMomentCommentList(@Param("commentIdList") List<Integer> commentIdList);
}
