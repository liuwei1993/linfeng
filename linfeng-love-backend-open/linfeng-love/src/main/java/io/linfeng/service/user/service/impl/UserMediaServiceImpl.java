package io.linfeng.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.service.user.dao.UserMediaDao;
import io.linfeng.service.user.entity.UserMediaEntity;
import io.linfeng.service.user.service.UserMediaService;
import org.springframework.stereotype.Service;


@Service("userMediaService")
public class UserMediaServiceImpl extends ServiceImpl<UserMediaDao, UserMediaEntity> implements UserMediaService {

}