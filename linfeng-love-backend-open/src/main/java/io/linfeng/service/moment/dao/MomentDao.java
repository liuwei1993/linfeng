package io.linfeng.service.moment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.linfeng.service.moment.dto.response.MomentResponseDTO;
import io.linfeng.service.moment.entity.MomentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 我的动态表
 * 
 * @author JiangCX
 * @date 2023-09-28 09:29:05
 */
@Mapper
public interface MomentDao extends BaseMapper<MomentEntity> {

    IPage<Integer> selectNewestMomentIdPage(IPage<Integer> page);
    List<MomentResponseDTO> selectNewestMomentList(@Param("momentIdList")List<Integer> momentIdList, @Param("uid")Integer uid);

    List<MomentResponseDTO> selectGuestMomentList(@Param("momentIdList")List<Integer> momentIdList, @Param("uid")Integer uid, @Param("guestUid")Integer guestUid);

    List<MomentResponseDTO> selectMyMomentList(@Param("momentIdList")List<Integer> momentIdList, @Param("uid")Integer uid);

    MomentResponseDTO selectMomentsDetail(@Param("uid")Integer uid, @Param("momentId")Integer momentId);

    void updateCv(@Param("momentId")Integer momentId, @Param("count")Integer count);

    void updateLv(@Param("momentId")Integer momentId, @Param("count")Integer count);

    void updatePv(@Param("momentId")Integer momentId);


}
