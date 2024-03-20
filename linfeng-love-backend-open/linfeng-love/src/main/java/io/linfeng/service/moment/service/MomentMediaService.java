package io.linfeng.service.moment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.service.moment.dto.response.MomentMediaResponseDTO;
import io.linfeng.service.moment.entity.MomentMediaEntity;

import java.util.List;

/**
 * 用户附件表
 *
 * @author JiangCX
 * @date 2023-10-12 09:53:39
 */
public interface MomentMediaService extends IService<MomentMediaEntity> {

    List<MomentMediaResponseDTO> getMomentImageList(Integer uid);
}

