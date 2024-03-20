package io.linfeng.service.moment.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.service.moment.dao.MomentMediaDao;
import io.linfeng.service.moment.dto.response.MomentMediaResponseDTO;
import io.linfeng.service.moment.entity.MomentMediaEntity;
import io.linfeng.service.moment.service.MomentMediaService;
import io.linfeng.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("momentMediaService")
public class MomentMediaServiceImpl extends ServiceImpl<MomentMediaDao, MomentMediaEntity> implements MomentMediaService {

    @Autowired
    UserService userService;

    @Override
    public List<MomentMediaResponseDTO> getMomentImageList(Integer uid) {
        return baseMapper.selectMomentImageList(uid);
    }

}