package io.linfeng.service.moment.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.service.moment.dao.MomentLikeDao;
import io.linfeng.service.moment.dto.response.MomentLikeResponseDTO;
import io.linfeng.service.moment.entity.MomentLikeEntity;
import io.linfeng.service.moment.service.MomentLikeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("momentLikeService")
public class MomentLikeServiceImpl extends ServiceImpl<MomentLikeDao, MomentLikeEntity> implements MomentLikeService {


}