package io.linfeng.service.moment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.moment.dto.response.MomentResponseDTO;
import io.linfeng.service.moment.entity.MomentEntity;

import java.util.Map;

/**
 * 我的动态表
 *
 * @author JiangCX
 * @date 2023-09-28 09:29:05
 */
public interface MomentService extends IService<MomentEntity> {

    PageObject queryPage(Map<String, Object> params);

    IPage<MomentResponseDTO> selectNewestMomentPage(IPage<MomentResponseDTO> page, Integer uid);

    IPage<MomentResponseDTO> selectGuestMomentPage(IPage<MomentResponseDTO> page, Integer uid, Integer guestUid);

    IPage<MomentResponseDTO> selectMyMomentPage(IPage<MomentResponseDTO> dtoPage, Integer uid);

    MomentResponseDTO selectMomentDetail(Integer uid, Integer momentId);

    void updateCv(Integer momentId, Integer operatorType);

    void updateLv(Integer momentId, Integer count);

    void updatePv(Integer momentId);

    void deleteMoment(Integer momentId);

}

