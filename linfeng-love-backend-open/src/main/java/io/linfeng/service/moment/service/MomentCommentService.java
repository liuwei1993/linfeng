package io.linfeng.service.moment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.api.PageObject;
import io.linfeng.service.moment.dto.response.MomentCommentResponseDTO;
import io.linfeng.service.moment.entity.MomentCommentEntity;

import java.util.Map;

/**
 * 动态评论表
 *
 * @author JiangCX
 * @date 2023-10-10 10:46:11
 */
public interface MomentCommentService extends IService<MomentCommentEntity> {

    PageObject queryPage(Map<String, Object> params);

    IPage<MomentCommentResponseDTO> getMomentCommentPage(IPage<MomentCommentResponseDTO> page, Integer momentId);

}

